package ru.practicum.shareit.booking.errors;

public class NotFoundBookingByUser extends RuntimeException {

    public NotFoundBookingByUser(String message) {
        super(message);
    }
}
