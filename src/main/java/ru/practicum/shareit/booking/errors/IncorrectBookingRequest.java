package ru.practicum.shareit.booking.errors;

public class IncorrectBookingRequest extends RuntimeException {
    public IncorrectBookingRequest(String message) {
        super(message);
    }
}
