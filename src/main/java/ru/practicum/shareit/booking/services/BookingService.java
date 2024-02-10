package ru.practicum.shareit.booking.services;

import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.dto.BookingDtoRequest;
import ru.practicum.shareit.booking.dto.BookingDtoResponse;

import java.util.Set;

@Service
public interface BookingService {
    BookingDtoResponse create(BookingDtoRequest bookingRequest, Long userId);

    BookingDtoResponse approve(Long bookingId, Long userId, boolean status);

    BookingDtoResponse get(Long bookingId, Long userId);

    Set<BookingDtoResponse> getByBooker(String bookingState, Long userId, Integer from, Integer size);

    Set<BookingDtoResponse> getByOwner(String bookingState, Long userId, Integer from, Integer size);
}
