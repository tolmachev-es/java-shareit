package ru.practicum.shareit.booking.errors;

public class UnsupportedMethod extends RuntimeException {
    public UnsupportedMethod(String message) {
        super(message);
    }
}
