package ru.practicum.shareit.booking.errors;

public class AlreadyCompleted extends RuntimeException {
    public AlreadyCompleted(String message) {
        super(message);
    }
}
