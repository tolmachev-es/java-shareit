package ru.practicum.shareit.booking.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.shareit.booking.model.BookingStatus;

import java.time.LocalDateTime;
import java.util.Set;

public interface BookingRepository extends JpaRepository<BookingEntity, Long> {
    Set<BookingEntity> findBookingEntitiesByBooker_Id(Long user_id);

    Set<BookingEntity> findBookingEntitiesByBooker_IdAndStartBeforeAndEndIsAfter(
            Long user_id, LocalDateTime now1, LocalDateTime now2);

    Set<BookingEntity> findBookingEntitiesByBooker_IdAndEndBefore(Long user_id, LocalDateTime now);

    Set<BookingEntity> findBookingEntitiesByBooker_IdAndStartAfter(Long user_id, LocalDateTime now);

    Set<BookingEntity> findBookingEntitiesByBooker_IdAndStatus(Long user_id, BookingStatus bookingStatus);

    Set<BookingEntity> findBookingEntitiesByItem_OwnerId(Long item_owner_id);

    Set<BookingEntity> findBookingEntitiesByItem_OwnerIdAndStartBeforeAndEndAfter
            (Long item_owner_id, LocalDateTime start, LocalDateTime end);

    Set<BookingEntity> findBookingEntitiesByItem_OwnerIdAndEndBefore(Long item_owner_id, LocalDateTime end);

    Set<BookingEntity> findBookingEntitiesByItem_OwnerIdAndStartAfter(Long item_owner_id, LocalDateTime start);

    Set<BookingEntity> findBookingEntitiesByItem_OwnerIdAndStatus(Long item_owner_id, BookingStatus status);
}
