package ru.practicum.shareit.booking.errors;

public class IncorrectBookingOwner extends RuntimeException {
    public IncorrectBookingOwner(String message) {
        super(message);
    }
}
