package ru.services.order.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.util.UriComponentsBuilder;
import ru.services.order.mapper.OrderMapper;
import ru.services.order.model.*;
import ru.services.order.service.MessageSenderService;
import ru.services.order.service.OrderService;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/order")
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;
    private final OrderMapper mapper;
    private final MessageSenderService messageSenderService;
    private final RestClient restClient;

    @Value("${service.billing.url}")
    private String billingUrl;

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

    @Operation(summary = "Delete order by id")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Order deleted"),
            @ApiResponse(responseCode = "404", description = "Order not found")
    })
    @DeleteMapping(value = "/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable("id") Long id) {
        orderService.deleteOrder(id);
    }

    @Operation(summary = "Create order")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Order created")
    })
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public OrderResponse create(@RequestBody @Valid OrderRequest request,
                                @RequestHeader HttpHeaders headers) {
        var order = orderService.createOrder(mapper.toOrder(request));
        NotificationRequest notification = new NotificationRequest(order.getUsername(), NotificationType.PUSH, "");

        // TODO: куда-нибудь перенести и нормально переписать
        var uri = UriComponentsBuilder
                .fromUriString(billingUrl)
                .path("/billing/credit")
                .queryParam("username", order.getUsername())
                .queryParam("amount", order.getAmount())
                .build().toUriString();

        try {
            var response = restClient.put()
                    .uri(uri)
                    .headers(httpHeaders -> httpHeaders.putAll(headers))
                    .retrieve()
                    .toEntity(String.class);

            if (response.getStatusCode().is2xxSuccessful()) {
                order.setStatus(OrderStatus.PAYED);
                orderService.setPayedStatus(order.getId());

                notification.setMessage("Order is created and has been payed");
            }
            else
                notification.setMessage("Order is created but not payed");

        } catch (RestClientResponseException e) {
            System.out.println(e.getResponseBodyAsString());
            notification.setMessage("Order is created but not payed");
        }

        messageSenderService.sendNotificationMessage(notification);
        return mapper.toResponse(order);
    }
}