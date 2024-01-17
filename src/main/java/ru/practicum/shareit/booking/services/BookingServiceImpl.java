package ru.practicum.shareit.booking.services;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.dao.BookingDao;
import ru.practicum.shareit.booking.dao.BookingEntity;
import ru.practicum.shareit.booking.dto.BookingDtoRequest;
import ru.practicum.shareit.booking.dto.BookingDtoResponse;
import ru.practicum.shareit.booking.errors.*;
import ru.practicum.shareit.booking.mappers.BookingMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.item.dao.ItemDao;
import ru.practicum.shareit.item.dao.ItemEntity;
import ru.practicum.shareit.item.mappers.ItemMapper;
import ru.practicum.shareit.user.dao.UserDao;
import ru.practicum.shareit.user.mappers.UserMapper;

import java.util.Set;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class BookingServiceImpl implements BookingService {
    private final BookingDao bookingDao;
    private final UserDao userDao;
    private final ItemDao itemDao;

    @Override
    public BookingDtoResponse create(BookingDtoRequest bookingRequest, Long userId) {
        Booking booking = BookingMapper.BOOKING_MAPPER.fromDto(bookingRequest);
        ItemEntity item = itemDao.get(bookingRequest.getItemId());
        if (item.getOwner().getId().equals(userId)) {
            throw new IncorrectBookingRequest("Владелец не может бронировать свою вещь");
        }
        if (item.getAvailable()) {
            if (bookingRequest.getStart().isAfter(bookingRequest.getEnd()) ||
                    bookingRequest.getStart().isEqual(bookingRequest.getEnd())) {
                throw new IncorrectBookingTime("Ошибка во времени бронирования");
            }

            booking.setItem(ItemMapper.ITEM_MAPPER.fromEntity(item));
            booking.setBooker(UserMapper.USER_MAPPER.fromEntity(userDao.getUserById(userId)));
            booking.setStatus(BookingStatus.WAITING);
            Booking createBooking = BookingMapper.BOOKING_MAPPER.fromEntity(
                    bookingDao.create(BookingMapper.BOOKING_MAPPER.toEntity(booking)));
            BookingDtoResponse bookingDtoResponse = BookingMapper.BOOKING_MAPPER.toDto(createBooking);
            return bookingDtoResponse;
        } else {
            throw new ItemNotAvailable(
                    String.format("Вещь с id %s недоступна для бронирования", bookingRequest.getItemId()));
        }
    }

    @Override
    public BookingDtoResponse approve(Long bookingId, Long userId, boolean status) {
        BookingEntity booking = bookingDao.get(bookingId);
        if (booking.getItem().getOwner().getId().equals(userId)) {
            if (booking.getStatus().equals(BookingStatus.APPROVED)) {
                throw new AlreadyCompleted("Статус уже изменен");
            }
            booking.setStatus(status ? BookingStatus.APPROVED : BookingStatus.REJECTED);
            Booking updateBooking = BookingMapper.BOOKING_MAPPER.fromEntity(bookingDao.update(booking));
            return BookingMapper.BOOKING_MAPPER.toDto(updateBooking);
        } else {
            throw new IncorrectBookingOwner(
                    String.format("Пользователь с id %s не может подтвердить запрос на бронирование", userId));
        }
    }

    @Override
    public BookingDtoResponse get(Long bookingId, Long userId) {
        Booking booking = BookingMapper.BOOKING_MAPPER.fromEntity(bookingDao.get(bookingId));
        if (booking.getItem().getOwner().getId().equals(userId) || booking.getBooker().getId().equals(userId)) {
            return BookingMapper.BOOKING_MAPPER.toDto(booking);
        } else {
            throw new IncorrectBookingRequest(String.format(
                    "Пользователь с id %s не имеет права на просмотр бронирования с id %s", userId, bookingId));
        }
    }

    @Override
    public Set<BookingDtoResponse> getByBooker(String bookingState, Long userId) {
        userDao.getUserById(userId);
        BookingState bookingStateEnum;
        try {
            bookingStateEnum = BookingState.valueOf(bookingState);
        } catch (Exception e) {
            throw new UnsupportedMethod(String.format("Unknown state: %s", bookingState));
        }
        Set<BookingEntity> result = null;
        switch (bookingStateEnum) {
            case ALL:
                result = bookingDao.getAllByBooker(userId);
                break;
            case PAST:
                result = bookingDao.getPastByBooker(userId);
                break;
            case FUTURE:
                result = bookingDao.getFutureByBooker(userId);
                break;
            case CURRENT:
                result = bookingDao.getCurrentByBooker(userId);
                break;
            case WAITING:
                result = bookingDao.getWaitingByBooker(userId);
                break;
            case REJECTED:
                result = bookingDao.getRejectedByBooker(userId);
                break;
        }
        return result.stream()
                .map(BookingMapper.BOOKING_MAPPER::fromEntity)
                .collect(Collectors.toSet())
                .stream()
                .map(BookingMapper.BOOKING_MAPPER::toDto)
                .collect(Collectors.toSet());
    }

    @Override
    public Set<BookingDtoResponse> getByOwner(String bookingState, Long userId) {
        userDao.getUserById(userId);
        BookingState bookingStateEnum;
        try {
            bookingStateEnum = BookingState.valueOf(bookingState);
        } catch (Exception e) {
            throw new UnsupportedMethod(String.format("Unknown state: %s", bookingState));
        }
        Set<BookingEntity> result = null;
        switch (bookingStateEnum) {
            case ALL:
                result = bookingDao.getAllByOwner(userId);
                break;
            case PAST:
                result = bookingDao.getPastByOwner(userId);
                break;
            case FUTURE:
                result = bookingDao.getFutureByOwner(userId);
                break;
            case CURRENT:
                result = bookingDao.getCurrentByOwner(userId);
                break;
            case WAITING:
                result = bookingDao.getWaitingByOwner(userId);
                break;
            case REJECTED:
                result = bookingDao.getRejectedByOwner(userId);
                break;
        }
        return result.stream()
                .map(BookingMapper.BOOKING_MAPPER::fromEntity)
                .collect(Collectors.toSet())
                .stream()
                .map(BookingMapper.BOOKING_MAPPER::toDto)
                .collect(Collectors.toSet());
    }
}
