package ru.services.user.authorization;

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
import ru.services.user.model.User;
import ru.services.user.model.UserRequest;
import ru.services.user.model.UserRole;
import ru.services.user.service.UserService;
import java.io.IOException;

@Component
@RequiredArgsConstructor
public class AuthorizationFilter extends OncePerRequestFilter {
    private final TokenService tokenService;
    private final UserService userService;
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
            // user'а ещё нет в системе, допускается создание пользователя только с тем же логином
            if (method.equals("POST"))
                return checkCreateAccess(client_id, path, request);

            User user = userService.getUser(client_id);

            if (user.role() == UserRole.ADMIN) return true;

            return switch (method) {
                case "PUT" -> checkUpdateAccess(user, path, request);
                case "DELETE" -> checkWriteAccess(user, path);
                case "GET" -> checkReadAccess(user, path, request);
                default -> false;
            };

        } catch (IOException | RuntimeException e) {
            return false;
        }
    }

    private boolean checkCreateAccess(String client_id, String path, CachedBodyHttpServletRequest request) throws IOException {
        UserRequest createRequest = objectMapper.readValue(
                request.getContentAsByteArray(),
                UserRequest.class
        );

        if (createRequest.username() == null) {
            return false;
        }

        return createRequest.username().equals(client_id);
    }

    private boolean checkUpdateAccess(User currentUser, String path, CachedBodyHttpServletRequest request) throws IOException {
        UserRequest updateRequest = objectMapper.readValue(
                request.getContentAsByteArray(),
                UserRequest.class
        );
        if (updateRequest.username() == null) {
            return false;
        }

        return updateRequest.username().equals(currentUser.username());
    }

    private boolean checkReadAccess(User user, String path, CachedBodyHttpServletRequest request) {
        if (path.startsWith("/users/")) {
            Long requestedId = extractIdFromPath(path);
            return requestedId != null && requestedId.equals(user.id());
        }

        if (path.equals("/users")) {
            String username = request.getParameter("username");
            return username != null && username.equals(user.username());
        }

        return false;
    }

    private boolean checkWriteAccess(User user, String path) {
        if (path.startsWith("/users/")) {
            Long requestedId = extractIdFromPath(path);
            return requestedId != null && requestedId.equals(user.id());
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