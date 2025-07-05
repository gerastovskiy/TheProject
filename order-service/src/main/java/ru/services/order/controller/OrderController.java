package ru.services.order.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.services.order.mapper.OrderMapper;
import ru.services.order.model.*;
import ru.services.order.service.OrderService;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/order")
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;
    private final OrderMapper mapper;

    @Operation(summary = "Get order by ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Order info"),
            @ApiResponse(responseCode = "404", description = "Order not found")
    })
    @GetMapping(value = "/{id}")
    public OrderResponse get(@PathVariable("id") Long id) {
        return mapper.toResponse(orderService.getOrder(id));
    }

    @Operation(summary = "Get orders list by username")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Orders info")
    })
    @GetMapping
    public List<OrderResponse> get(@RequestParam String username) {
        return orderService.getOrdersList(username).stream().map(mapper::toResponse).collect(Collectors.toList());
    }

    @Operation(summary = "Create order")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Order created")
    })
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public OrderResponse create(@RequestBody @Valid OrderRequest request,
                                @RequestHeader HttpHeaders headers) {
        return mapper.toResponse(orderService.createOrder(mapper.toOrder(request)));
    }

    @Operation(summary = "Reject order by id")
    @ApiResponses({
            @ApiResponse(responseCode = "202", description = "Order reject request accepted")
    })
    @DeleteMapping(value = "/{id}/reject")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void reject(@PathVariable("id") Long id) {
        orderService.createOrderReject(id);
    }
}