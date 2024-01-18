package ru.practicum.shareit.booking.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.shareit.booking.model.BookingStatus;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Set;

public interface BookingRepository extends JpaRepository<BookingEntity, Long> {
    Set<BookingEntity> findBookingEntitiesByBooker_Id(Long userid);

    Set<BookingEntity> findBookingEntitiesByBooker_IdAndStartBeforeAndEndIsAfter(
            Long userId, LocalDateTime now1, LocalDateTime now2);

    Set<BookingEntity> findBookingEntitiesByBooker_IdAndEndBefore(Long userid, LocalDateTime now);

    Set<BookingEntity> findBookingEntitiesByBooker_IdAndStartAfter(Long userid, LocalDateTime now);

    Set<BookingEntity> findBookingEntitiesByBooker_IdAndStatus(Long userid, BookingStatus bookingStatus);

    Set<BookingEntity> findBookingEntitiesByItem_OwnerId(Long itemOwnerId);

    Set<BookingEntity> findBookingEntitiesByItem_OwnerIdAndStartBeforeAndEndAfter(
            Long itemOwnerId, LocalDateTime start, LocalDateTime end);

    Set<BookingEntity> findBookingEntitiesByItem_OwnerIdAndEndBefore(Long itemOwnerId, LocalDateTime end);

    Set<BookingEntity> findBookingEntitiesByItem_OwnerIdAndStartAfter(Long itemOwnerId, LocalDateTime start);

    Set<BookingEntity> findBookingEntitiesByItem_OwnerIdAndStatus(Long itemOwnerId, BookingStatus status);

    Optional<BookingEntity> findTopBookingEntitiesByItem_IdAndStartBeforeAndStatusOrderByEndDesc(
            Long itemId, LocalDateTime end, BookingStatus status);

    Optional<BookingEntity> findTopBookingEntitiesByItem_IdAndStartAfterAndStatusOrderByStartAsc(
            Long itemId, LocalDateTime start, BookingStatus status);

    Optional<BookingEntity> findTopBookingEntitiesByItem_IdAndBooker_IdAndEndBefore(
            Long itemId, Long bookerId, LocalDateTime end);
}