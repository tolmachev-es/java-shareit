package ru.practicum.shareit.request.exception;

public class ItemRequestNotFound extends RuntimeException{
    public ItemRequestNotFound(String message) {
        super(message);
    }
}
