package ru.practicum.shareit.booking.dao;

import lombok.Data;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.booking.errors.BookingNotFound;
import ru.practicum.shareit.booking.errors.NotFoundBookingByUser;
import ru.practicum.shareit.booking.model.BookingStatus;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Set;


@Data
@Repository
public class DBBookingDao implements BookingDao {
    private final BookingRepository bookingRepository;

    @Override
    @Transactional
    public BookingEntity create(BookingEntity bookingEntity) {
        bookingRepository.save(bookingEntity);
        return bookingEntity;
    }

    @Override
    public BookingEntity get(Long bookingId) {
        Optional<BookingEntity> booking = bookingRepository.findById(bookingId);
        if (booking.isPresent()) {
            return booking.get();
        } else {
            throw new BookingNotFound(String.format("Бронирование с id %s не найдено", bookingId));
        }
    }

    @Override
    public BookingEntity update(BookingEntity bookingEntity) {
        return bookingRepository.save(bookingEntity);
    }

    @Override
    public Set<BookingEntity> getAllByBooker(Long userId) {
        return bookingRepository.findBookingEntitiesByBooker_Id(userId);
    }

    @Override
    public Set<BookingEntity> getCurrentByBooker(Long userId) {
        return bookingRepository.findBookingEntitiesByBooker_IdAndStartBeforeAndEndIsAfter(userId,
                LocalDateTime.now(),
                LocalDateTime.now());
    }

    @Override
    public Set<BookingEntity> getPastByBooker(Long userId) {
        return bookingRepository.findBookingEntitiesByBooker_IdAndEndBefore(userId, LocalDateTime.now());
    }

    @Override
    public Set<BookingEntity> getFutureByBooker(Long userId) {
        return bookingRepository.findBookingEntitiesByBooker_IdAndStartAfter(userId, LocalDateTime.now());
    }

    @Override
    public Set<BookingEntity> getWaitingByBooker(Long userId) {
        return bookingRepository.findBookingEntitiesByBooker_IdAndStatus(userId, BookingStatus.WAITING);
    }

    @Override
    public Set<BookingEntity> getRejectedByBooker(Long userId) {
        return bookingRepository.findBookingEntitiesByBooker_IdAndStatus(userId, BookingStatus.REJECTED);
    }

    @Override
    public Set<BookingEntity> getAllByOwner(Long userId) {
        return bookingRepository.findBookingEntitiesByItem_OwnerId(userId);
    }

    @Override
    public Set<BookingEntity> getCurrentByOwner(Long userId) {
        return bookingRepository.findBookingEntitiesByItem_OwnerIdAndStartBeforeAndEndAfter(userId,
                LocalDateTime.now(), LocalDateTime.now());
    }

    @Override
    public Set<BookingEntity> getPastByOwner(Long userId) {
        return bookingRepository.findBookingEntitiesByItem_OwnerIdAndEndBefore(userId, LocalDateTime.now());
    }

    @Override
    public Set<BookingEntity> getFutureByOwner(Long userId) {
        return bookingRepository.findBookingEntitiesByItem_OwnerIdAndStartAfter(userId, LocalDateTime.now());
    }

    @Override
    public Set<BookingEntity> getWaitingByOwner(Long userId) {
        return bookingRepository.findBookingEntitiesByItem_OwnerIdAndStatus(userId, BookingStatus.WAITING);
    }

    @Override
    public Set<BookingEntity> getRejectedByOwner(Long userId) {
        return bookingRepository.findBookingEntitiesByItem_OwnerIdAndStatus(userId, BookingStatus.REJECTED);
    }

    @Override
    public Optional<BookingEntity> getNextBooking(Long itemId) {
        return bookingRepository.findTopBookingEntitiesByItem_IdAndStartAfterAndStatusOrderByStartAsc(
                itemId, LocalDateTime.now(), BookingStatus.APPROVED);

    }

    @Override
    public Optional<BookingEntity> getLastBooking(Long itemId) {
        return bookingRepository.findTopBookingEntitiesByItem_IdAndStartBeforeAndStatusOrderByEndDesc(
                itemId, LocalDateTime.now(), BookingStatus.APPROVED);

    }

    @Override
    public BookingEntity getBookingByIdAndBooker(Long itemId, Long userId) {
        Optional<BookingEntity> booking = bookingRepository
                .findTopBookingEntitiesByItem_IdAndBooker_IdAndEndBefore(itemId, userId, LocalDateTime.now());
        if (booking.isPresent()) {
            return booking.get();
        } else {
            throw new NotFoundBookingByUser("По параметрам не найдено бронирование");
        }
    }

}
