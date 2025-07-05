package ru.services.delivery.exception;

public class DuplicateDeliveryException extends RuntimeException{
    public DuplicateDeliveryException(Long order_id) {
        super("Delivery order with id " + order_id + " already exists");
    }
}