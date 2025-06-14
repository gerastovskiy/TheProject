package ru.services.billing.authorization;

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
import ru.services.billing.model.Account;
import ru.services.billing.model.AccountRequest;
import ru.services.billing.service.AccountService;
import java.io.IOException;

@Component
@RequiredArgsConstructor
public class AuthorizationFilter extends OncePerRequestFilter {
    private final TokenService tokenService;
    private final AccountService accountService;
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
            // account'а ещё нет в системе, допускается создание пользователя только с тем же логином
            if (method.equals("POST"))
                return checkCreateAccess(client_id, path, request);

            Account account = accountService.getAccount(client_id);

            return switch (method) {
                case "PUT" -> checkUpdateAccess(account, path, request);
                default -> false;
            };

        } catch (IOException | RuntimeException e) {
            return false;
        }
    }

    private boolean checkCreateAccess(String client_id, String path, CachedBodyHttpServletRequest request) throws IOException {
        AccountRequest createRequest = objectMapper.readValue(
                request.getContentAsByteArray(),
                AccountRequest.class
        );

        if (createRequest.username() == null) {
            return false;
        }

        return createRequest.username().equals(client_id);
    }

    private boolean checkUpdateAccess(Account account, String path, CachedBodyHttpServletRequest request) {
        String username = request.getParameter("username");
        return username != null && username.equals(account.username());
    }

}