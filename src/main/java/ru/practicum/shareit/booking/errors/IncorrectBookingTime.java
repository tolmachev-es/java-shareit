package ru.practicum.shareit.booking.errors;

public class IncorrectBookingTime extends RuntimeException {
    public IncorrectBookingTime(String message) {
        super(message);
    }
}
