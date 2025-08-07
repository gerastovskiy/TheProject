package ru.services.order.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import ru.services.order.authorization.AccessControlService;
import ru.services.order.mapper.OrderMapper;
import ru.services.order.model.*;
import ru.services.order.service.OrderService;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/api/orders/v1")
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;
    private final AccessControlService accessControlService;
    private final OrderMapper mapper;

    @Operation(summary = "Get order by ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Order info"),
            @ApiResponse(responseCode = "404", description = "Order not found")
    })
    @GetMapping(value = "/{id}")
    @PreAuthorize("isAuthenticated()")
    public OrderResponse get(
            @PathVariable("id") Long id,
            Authentication authentication) {

        String clientId = authentication.getName();
        String username = orderService.getOrder(id).getUsername();

        if (!accessControlService.checkUsernameMatches(clientId, username)) {
            throw new AccessDeniedException("Access denied");
        }

        return mapper.toResponse(orderService.getOrder(id));
    }

    @Operation(summary = "Get orders list by username")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Orders info")
    })
    @GetMapping
    @PreAuthorize("isAuthenticated()")
    public List<OrderResponse> get(
            @RequestParam String username,
            Authentication authentication) {

        String clientId = authentication.getName();

        if (!accessControlService.checkUsernameMatches(clientId, username)) {
            throw new AccessDeniedException("Access denied");
        }

        return orderService.getOrdersList(username).stream().map(mapper::toResponse).collect(Collectors.toList());
    }

    @Operation(summary = "Create order")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Order created")
    })
    @PostMapping
    @PreAuthorize("isAuthenticated()")
    @ResponseStatus(HttpStatus.CREATED)
    public OrderResponse create(
            @RequestBody @Valid OrderRequest request,
            @RequestHeader("X-Request-ID") String idempotencyKey,
            @RequestHeader HttpHeaders headers,
            Authentication authentication) {

        String clientId = authentication.getName();

        if (!accessControlService.checkUsernameMatches(clientId, request.username())) {
            throw new AccessDeniedException("Access denied");
        }

        headers.forEach((key, value) ->
                log.info("Header: {} = {}", key, value));

        return mapper.toResponse(orderService.createOrder(mapper.toOrder(request), idempotencyKey));
    }

    @Operation(summary = "Reject order by id")
    @ApiResponses({
            @ApiResponse(responseCode = "202", description = "Order reject request accepted")
    })
    @DeleteMapping(value = "/{id}/reject")
    @PreAuthorize("isAuthenticated()")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void reject(
            @PathVariable("id") Long id,
            Authentication authentication) {

        String clientId = authentication.getName();
        String username = orderService.getOrder(id).getUsername();

        if (!accessControlService.checkUsernameMatches(clientId, username)) {
            throw new AccessDeniedException("Access denied");
        }

        orderService.createOrderReject(id);
    }
}