package ru.practicum.shareit.booking.dao;

import java.util.Optional;
import java.util.Set;

public interface BookingDao {
    BookingEntity create(BookingEntity bookingEntity);

    BookingEntity get(Long bookingId);

    BookingEntity update(BookingEntity bookingEntity);

    Set<BookingEntity> getAllByBooker(Long userId);

    Set<BookingEntity> getCurrentByBooker(Long userId);

    Set<BookingEntity> getPastByBooker(Long userId);

    Set<BookingEntity> getFutureByBooker(Long userId);

    Set<BookingEntity> getWaitingByBooker(Long userId);

    Set<BookingEntity> getRejectedByBooker(Long userId);

    Set<BookingEntity> getAllByOwner(Long userId);

    Set<BookingEntity> getCurrentByOwner(Long userId);

    Set<BookingEntity> getPastByOwner(Long userId);

    Set<BookingEntity> getFutureByOwner(Long userId);

    Set<BookingEntity> getWaitingByOwner(Long userId);

    Set<BookingEntity> getRejectedByOwner(Long userId);
    Optional<BookingEntity> getNextBooking(Long itemId);
    Optional<BookingEntity> getLastBooking(Long itemId);
}
