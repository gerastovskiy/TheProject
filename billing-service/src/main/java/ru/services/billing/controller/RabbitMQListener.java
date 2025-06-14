package ru.services.billing.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import ru.services.billing.mapper.AccountMapper;
import ru.services.billing.model.UserResponse;
import ru.services.billing.service.AccountService;

@Component
@RequiredArgsConstructor
public class RabbitMQListener {
    private final AccountService accountService;
    private final AccountMapper mapper;

    @RabbitListener(queues = "#{@environment.getProperty('rabbitmq.queue.name')}")
    public void receiveMessage(UserResponse request) {
        // TODO: реализовать обработку ошибок
        try {
            var account = accountService.createAccount(mapper.toAccount(request));
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}
