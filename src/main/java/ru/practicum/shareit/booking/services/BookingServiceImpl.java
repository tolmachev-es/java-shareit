package ru.practicum.shareit.booking.services;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.HeadExeptions.AlreadyExistException;
import ru.practicum.shareit.HeadExeptions.InvalidParameterException;
import ru.practicum.shareit.HeadExeptions.ObjectNotFound;
import ru.practicum.shareit.booking.dao.BookingEntity;
import ru.practicum.shareit.booking.dao.BookingRepository;
import ru.practicum.shareit.booking.dto.BookingDtoRequest;
import ru.practicum.shareit.booking.dto.BookingDtoResponse;
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
@EnableJpaRepositories(basePackages = {"ru.practicum.shareit.booking.dao"})
public class BookingServiceImpl implements BookingService {
    private final UserDao userDao;
    private final ItemDao itemDao;
    private final BookingRepository bookingRepositoryFromBookingService;

    @Override
    public BookingDtoResponse create(BookingDtoRequest bookingRequest, Long userId) {
        Booking booking = BookingMapper.BOOKING_MAPPER.fromDto(bookingRequest);
        ItemEntity item = itemDao.get(bookingRequest.getItemId());
        if (item.getOwner().getId().equals(userId)) {
            throw new ObjectNotFound("Владелец не может бронировать свою вещь");
        }
        if (item.getAvailable()) {
            if (bookingRequest.getStart().isAfter(bookingRequest.getEnd()) ||
                    bookingRequest.getStart().isEqual(bookingRequest.getEnd())) {
                throw new InvalidParameterException("Ошибка во времени бронирования");
            }
            booking.setItem(ItemMapper.ITEM_MAPPER.fromEntity(item));
            booking.setBooker(UserMapper.USER_MAPPER.fromEntity(userDao.getUserById(userId)));
            booking.setStatus(BookingStatus.WAITING);
            BookingEntity createBooking = BookingMapper.BOOKING_MAPPER.toEntity(booking);
            return BookingMapper.BOOKING_MAPPER.toDto(
                    BookingMapper.BOOKING_MAPPER.fromEntity(
                            bookingRepositoryFromBookingService.save(createBooking)));
        } else {
            throw new InvalidParameterException(
                    String.format("Вещь с id %s недоступна для бронирования", bookingRequest.getItemId()));
        }
    }

    @Override
    public BookingDtoResponse approve(Long bookingId, Long userId, boolean status) {
        Optional<BookingEntity> findBooking = bookingRepositoryFromBookingService.findById(bookingId);
        if (findBooking.isPresent()) {
            if (findBooking.get().getItem().getOwner().getId().equals(userId)) {
                if (findBooking.get().getStatus().equals(BookingStatus.APPROVED)) {
                    throw new AlreadyExistException("Статус уже изменен");
                }
                findBooking.get().setStatus(status ? BookingStatus.APPROVED : BookingStatus.REJECTED);
                return BookingMapper.BOOKING_MAPPER.toDto(
                        BookingMapper.BOOKING_MAPPER.fromEntity(
                                bookingRepositoryFromBookingService.save(findBooking.get())));
            } else {
                throw new ObjectNotFound(
                        String.format("Не найдено бронирование которое может подтвердить пользователь с id %s", userId));
            }
        } else {
            throw new ObjectNotFound(String.format("Бронирование с id %s не найдено", bookingId));
        }

    }

    @Override
    public BookingDtoResponse get(Long bookingId, Long userId) {
        Optional<BookingEntity> findBooking = bookingRepositoryFromBookingService.findById(bookingId);
        if (findBooking.isPresent()) {
            if (findBooking.get().getItem().getOwner().getId().equals(userId)
                    || findBooking.get().getBooker().getId().equals(userId)) {
                return BookingMapper.BOOKING_MAPPER.toDto(BookingMapper.BOOKING_MAPPER.fromEntity(findBooking.get()));
            } else {
                throw new ObjectNotFound(String.format(
                        "Пользователь с id %s не найден среди участников бронирования с id %s", userId, bookingId));
            }
        } else {
            throw new ObjectNotFound(String.format("Бронирование с id %s не найдено", bookingId));
        }

    }

    @Override
    public Set<BookingDtoResponse> getByBooker(String bookingState, Long userId, Integer from, Integer size) {
        userDao.getUserById(userId);
        BookingState bookingStateEnum;
        try {
            bookingStateEnum = BookingState.valueOf(bookingState);
        } catch (Exception e) {
            throw new InvalidParameterException(String.format("Unknown state: %s", bookingState));
        }
        Set<BookingEntity> result = null;
        Pageable pageable = PageRequest.of(from / size, size);
        switch (bookingStateEnum) {
            case ALL:
                result = bookingRepositoryFromBookingService.findBookingEntitiesByBooker_IdOrderByIdDesc(
                        userId, pageable).toSet();
                break;
            case PAST:
                result = bookingRepositoryFromBookingService.findBookingEntitiesByBooker_IdAndEndBefore(
                        userId, LocalDateTime.now(), pageable).toSet();
                break;
            case FUTURE:
                result = bookingRepositoryFromBookingService.findBookingEntitiesByBooker_IdAndStartAfter(
                        userId, LocalDateTime.now(), pageable).toSet();
                break;
            case CURRENT:
                result = bookingRepositoryFromBookingService.findBookingEntitiesByBooker_IdAndStartBeforeAndEndIsAfter(
                        userId, LocalDateTime.now(), LocalDateTime.now(), pageable).toSet();
                break;
            case WAITING:
                result = bookingRepositoryFromBookingService.findBookingEntitiesByBooker_IdAndStatus(
                        userId, BookingStatus.WAITING, pageable).toSet();
                break;
            case REJECTED:
                result = bookingRepositoryFromBookingService.findBookingEntitiesByBooker_IdAndStatus(
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
            throw new InvalidParameterException(String.format("Unknown state: %s", bookingState));
        }
        Set<BookingEntity> result = null;
        switch (bookingStateEnum) {
            case ALL:
                result = bookingRepositoryFromBookingService.findBookingEntitiesByItem_OwnerIdOrderByIdDesc(
                        userId, pageable).toSet();
                break;
            case PAST:
                result = bookingRepositoryFromBookingService.findBookingEntitiesByItem_OwnerIdAndEndBefore(
                        userId, LocalDateTime.now(), pageable).toSet();
                break;
            case FUTURE:
                result = bookingRepositoryFromBookingService.findBookingEntitiesByItem_OwnerIdAndStartAfter(
                        userId, LocalDateTime.now(), pageable).toSet();
                break;
            case CURRENT:
                result = bookingRepositoryFromBookingService.findBookingEntitiesByItem_OwnerIdAndStartBeforeAndEndAfter(
                        userId, LocalDateTime.now(), LocalDateTime.now(), pageable).toSet();
                break;
            case WAITING:
                result = bookingRepositoryFromBookingService.findBookingEntitiesByItem_OwnerIdAndStatus(
                        userId, BookingStatus.WAITING, pageable).toSet();
                break;
            case REJECTED:
                result = bookingRepositoryFromBookingService.findBookingEntitiesByItem_OwnerIdAndStatus(
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
