package ru.services.order.authorization;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import ru.services.order.model.Order;
import ru.services.order.model.OrderRequest;
import ru.services.order.service.OrderService;
import java.io.IOException;

@Component
@RequiredArgsConstructor
public class AuthorizationFilter extends OncePerRequestFilter {
    private final TokenService tokenService;
    private final OrderService orderService;
    private final ObjectMapper objectMapper;

    @Value("${auth.enabled}")
    private boolean authEnabled;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {

        if (!authEnabled) {
            filterChain.doFilter(request, response);
            return;
        }

        String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Missing token");
            return;
        }

        TokenValidationResult tokenResult = tokenService.validateToken(authHeader.substring(7));
        if (!tokenResult.isValid()) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "Invalid token");
            return;
        }

        CachedBodyHttpServletRequest wrappedRequest = new CachedBodyHttpServletRequest(request);
        if (!hasAccess(tokenResult.client_id(), wrappedRequest)) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "Access denied");
            return;
        }

        filterChain.doFilter(wrappedRequest, response);
    }

    private boolean hasAccess(String client_id, CachedBodyHttpServletRequest request
    ) {
        String method = request.getMethod();
        String path = request.getRequestURI();

        try {
            if (method.equals("POST"))
                return checkCreateAccess(client_id, path, request);

            Order order = orderService.getOrdersList(client_id).getFirst();

            return switch (method) {
                case "PUT" -> checkUpdateAccess(order, path, request);
                case "DELETE" -> checkWriteAccess(order, path);
                case "GET" -> checkReadAccess(order, path, request);
                default -> false;
            };

        } catch (IOException | RuntimeException e) {
            return false;
        }
    }

    private boolean checkCreateAccess(String client_id, String path, CachedBodyHttpServletRequest request) throws IOException {
        OrderRequest createRequest = objectMapper.readValue(
                request.getContentAsByteArray(),
                OrderRequest.class
        );

        if (createRequest.username() == null) {
            return false;
        }

        return createRequest.username().equals(client_id);
    }

    private boolean checkUpdateAccess(Order currentOrder, String path, CachedBodyHttpServletRequest request) throws IOException {
        OrderRequest updateRequest = objectMapper.readValue(
                request.getContentAsByteArray(),
                OrderRequest.class
        );
        if (updateRequest.username() == null) {
            return false;
        }

        return updateRequest.username().equals(currentOrder.getUsername());
    }

    private boolean checkReadAccess(Order order, String path, CachedBodyHttpServletRequest request) {
        if (path.startsWith("/order/")) {
            Long requestedId = extractIdFromPath(path);
            return requestedId != null && requestedId.equals(order.getId());
        }

        if (path.equals("/order")) {
            String username = request.getParameter("username");
            return username != null && username.equals(order.getUsername());
        }

        return false;
    }

    private boolean checkWriteAccess(Order order, String path) {
        if (path.startsWith("/order/")) {
            Long requestedId = extractIdFromPath(path);
            return requestedId != null && requestedId.equals(order.getId());
        }
        return false;
    }

    private Long extractIdFromPath(String path) {
        try {
            String idPart = path.substring(path.lastIndexOf("/") + 1);
            return Long.parseLong(idPart);
        } catch (NumberFormatException | StringIndexOutOfBoundsException e) {
            return null;
        }
    }
}