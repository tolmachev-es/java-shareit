package ru.practicum.shareit.booking.dao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.shareit.booking.model.BookingStatus;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface BookingRepository extends JpaRepository<BookingEntity, Long> {
    Page<BookingEntity> findBookingEntitiesByBooker_IdOrderByIdDesc(
            Long userid, Pageable pageable);

    Page<BookingEntity> findBookingEntitiesByBooker_IdAndStartBeforeAndEndIsAfter(
            Long userId, LocalDateTime now1, LocalDateTime now2, Pageable pageable);

    Page<BookingEntity> findBookingEntitiesByBooker_IdAndEndBefore(
            Long userid, LocalDateTime now, Pageable pageable);

    Page<BookingEntity> findBookingEntitiesByBooker_IdAndStartAfter(
            Long userid, LocalDateTime now, Pageable pageable);

    Page<BookingEntity> findBookingEntitiesByBooker_IdAndStatus(
            Long userid, BookingStatus bookingStatus, Pageable pageable);

    Page<BookingEntity> findBookingEntitiesByItem_OwnerIdOrderByIdDesc(
            Long itemOwnerId, Pageable pageable);

    Page<BookingEntity> findBookingEntitiesByItem_OwnerIdAndStartBeforeAndEndAfter(
            Long itemOwnerId, LocalDateTime start, LocalDateTime end, Pageable pageable);

    Page<BookingEntity> findBookingEntitiesByItem_OwnerIdAndEndBefore(
            Long itemOwnerId, LocalDateTime end, Pageable pageable);

    Page<BookingEntity> findBookingEntitiesByItem_OwnerIdAndStartAfter(
            Long itemOwnerId, LocalDateTime start, Pageable pageable);

    Page<BookingEntity> findBookingEntitiesByItem_OwnerIdAndStatus(
            Long itemOwnerId, BookingStatus status, Pageable pageable);

    Optional<BookingEntity> findTopBookingEntitiesByItem_IdAndStartBeforeAndStatusOrderByEndDesc(
            Long itemId, LocalDateTime end, BookingStatus status);

    Optional<BookingEntity> findTopBookingEntitiesByItem_IdAndStartAfterAndStatusOrderByStartAsc(
            Long itemId, LocalDateTime start, BookingStatus status);

    Optional<BookingEntity> findTopBookingEntitiesByItem_IdAndBooker_IdAndEndBefore(
            Long itemId, Long bookerId, LocalDateTime end);

}
