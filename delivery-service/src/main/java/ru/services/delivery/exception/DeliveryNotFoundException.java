package ru.services.delivery.exception;

public class DeliveryNotFoundException extends RuntimeException {
    public DeliveryNotFoundException(Long id) {
        super("Delivery not found with id: " + id);
    }
}