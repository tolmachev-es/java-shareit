package ru.practicum.shareit.booking.errors;

public class ItemNotAvailable extends RuntimeException {
    public ItemNotAvailable(String message) {
        super(message);
    }
}
