package ru.practicum.shareit.booking.services;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.dao.BookingEntity;
import ru.practicum.shareit.booking.dao.BookingRepository;
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

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class BookingServiceImpl implements BookingService {
    private final UserDao userDao;
    private final ItemDao itemDao;
    private final BookingRepository bookingRepository;

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
            BookingEntity createBooking = BookingMapper.BOOKING_MAPPER.toEntity(booking);
            return BookingMapper.BOOKING_MAPPER.toDto(
                    BookingMapper.BOOKING_MAPPER.fromEntity(
                            bookingRepository.save(createBooking)));
        } else {
            throw new ItemNotAvailable(
                    String.format("Вещь с id %s недоступна для бронирования", bookingRequest.getItemId()));
        }
    }

    @Override
    public BookingDtoResponse approve(Long bookingId, Long userId, boolean status) {
        Optional<BookingEntity> findBooking = bookingRepository.findById(bookingId);
        if (findBooking.isPresent()) {
            if (findBooking.get().getItem().getOwner().getId().equals(userId)) {
                if (findBooking.get().getStatus().equals(BookingStatus.APPROVED)) {
                    throw new AlreadyCompleted("Статус уже изменен");
                }
                findBooking.get().setStatus(status ? BookingStatus.APPROVED : BookingStatus.REJECTED);
                return BookingMapper.BOOKING_MAPPER.toDto(
                        BookingMapper.BOOKING_MAPPER.fromEntity(
                                bookingRepository.save(findBooking.get())));
            } else {
                throw new IncorrectBookingOwner(
                        String.format("Пользователь с id %s не может подтвердить запрос на бронирование", userId));
            }
        } else {
            throw new BookingNotFound(String.format("Бронирование с id %s не найдено", bookingId));
        }

    }

    @Override
    public BookingDtoResponse get(Long bookingId, Long userId) {
        Optional<BookingEntity> findBooking = bookingRepository.findById(bookingId);
        if (findBooking.isPresent()) {
            if (findBooking.get().getItem().getOwner().getId().equals(userId)
                    || findBooking.get().getBooker().getId().equals(userId)) {
                return BookingMapper.BOOKING_MAPPER.toDto(BookingMapper.BOOKING_MAPPER.fromEntity(findBooking.get()));
            } else {
                throw new IncorrectBookingRequest(String.format(
                        "Пользователь с id %s не имеет права на просмотр бронирования с id %s", userId, bookingId));
            }
        } else {
            throw new BookingNotFound(String.format("Бронирование с id %s не найдено", bookingId));
        }

    }

    @Override
    public Set<BookingDtoResponse> getByBooker(String bookingState, Long userId, Integer from, Integer size) {
        userDao.getUserById(userId);
        BookingState bookingStateEnum;
        try {
            bookingStateEnum = BookingState.valueOf(bookingState);
        } catch (Exception e) {
            throw new UnsupportedMethod(String.format("Unknown state: %s", bookingState));
        }
        Set<BookingEntity> result = null;
        Pageable pageable = PageRequest.of(from / size, size);
        switch (bookingStateEnum) {
            case ALL:
                result = bookingRepository.findBookingEntitiesByBooker_IdOrderByIdDesc(
                        userId, pageable).toSet();
                break;
            case PAST:
                result = bookingRepository.findBookingEntitiesByBooker_IdAndEndBefore(
                        userId, LocalDateTime.now(), pageable).toSet();
                break;
            case FUTURE:
                result = bookingRepository.findBookingEntitiesByBooker_IdAndStartAfter(
                        userId, LocalDateTime.now(), pageable).toSet();
                break;
            case CURRENT:
                result = bookingRepository.findBookingEntitiesByBooker_IdAndStartBeforeAndEndIsAfter(
                        userId, LocalDateTime.now(), LocalDateTime.now(), pageable).toSet();
                break;
            case WAITING:
                result = bookingRepository.findBookingEntitiesByBooker_IdAndStatus(
                        userId, BookingStatus.WAITING, pageable).toSet();
                break;
            case REJECTED:
                result = bookingRepository.findBookingEntitiesByBooker_IdAndStatus(
                        userId, BookingStatus.REJECTED, pageable).toSet();
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
    public Set<BookingDtoResponse> getByOwner(String bookingState, Long userId, Integer from, Integer size) {
        userDao.getUserById(userId);
        BookingState bookingStateEnum;
        Pageable pageable = PageRequest.of(from / size, size);
        try {
            bookingStateEnum = BookingState.valueOf(bookingState);
        } catch (Exception e) {
            throw new UnsupportedMethod(String.format("Unknown state: %s", bookingState));
        }
        Set<BookingEntity> result = null;
        switch (bookingStateEnum) {
            case ALL:
                result = bookingRepository.findBookingEntitiesByItem_OwnerIdOrderByIdDesc(
                        userId, pageable).toSet();
                break;
            case PAST:
                result = bookingRepository.findBookingEntitiesByItem_OwnerIdAndEndBefore(
                        userId, LocalDateTime.now(), pageable).toSet();
                break;
            case FUTURE:
                result = bookingRepository.findBookingEntitiesByItem_OwnerIdAndStartAfter(
                        userId, LocalDateTime.now(), pageable).toSet();
                break;
            case CURRENT:
                result = bookingRepository.findBookingEntitiesByItem_OwnerIdAndStartBeforeAndEndAfter(
                        userId, LocalDateTime.now(), LocalDateTime.now(), pageable).toSet();
                break;
            case WAITING:
                result = bookingRepository.findBookingEntitiesByItem_OwnerIdAndStatus(
                        userId, BookingStatus.WAITING, pageable).toSet();
                break;
            case REJECTED:
                result = bookingRepository.findBookingEntitiesByItem_OwnerIdAndStatus(
                        userId, BookingStatus.REJECTED, pageable).toSet();
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
