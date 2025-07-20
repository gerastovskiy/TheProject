package ru.services.billing.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import ru.core.commands.*;
import ru.services.billing.mapper.AccountMapper;
import ru.services.billing.service.AccountService;

@Component
@RequiredArgsConstructor
@RabbitListener(queues = "#{@environment.getProperty('rabbitmq.queue.name')}")
public class RabbitMQController {
    @Value("${rabbitmq.processed-routing-key.name}")
    private String processedRoutingKey;
    private final RabbitTemplate rabbitTemplate;
    private final AccountService accountService;
    private final AccountMapper mapper;

    @RabbitHandler
    public void handleCreateBillingAccountCommand(CreateBillingAccountCommand command) {
        // TODO: реализовать обработку ошибок
        try {
            var account = accountService.createAccount(mapper.toAccount(command));
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    @RabbitHandler
    public void handleProcessPaymentCommand(ProcessPaymentCommand command) {
        // TODO: реализовать обработку ошибок
        try {
            var account = accountService.creditAccount(command.getUsername(), command.getAmount(), "");
            rabbitTemplate.convertAndSend(processedRoutingKey, mapper.toPaymentProcessedEvent(account, command.getOrderId()));
        }
        catch (Exception e) {
            rabbitTemplate.convertAndSend(processedRoutingKey, mapper.toPaymentFailedEvent(command.getUsername(), command.getOrderId(), e.getMessage()));
        }
    }

    @RabbitHandler
    public void handleProcessPaymentCommand(CancelPaymentCommand command) {
        // TODO: реализовать обработку ошибок
        try {
            var account = accountService.debitAccount(command.getUsername(), command.getAmount(), "");
            rabbitTemplate.convertAndSend(processedRoutingKey, mapper.toPaymentCalcelledEvent(account, command.getOrderId()));
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}
