package ru.services.store.exception;

public class DuplicateProductException extends RuntimeException{
    public DuplicateProductException(Integer id) {
        super("Product with " + id + " already exists");
    }
}