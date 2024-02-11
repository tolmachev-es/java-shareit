package ru.practicum.shareit.booking.dto;

import lombok.Data;
import ru.practicum.shareit.booking.model.validationGroups.BookingOnCreate;

import javax.validation.constraints.Future;
import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

/**
 * TODO Sprint add-bookings.
 */
@Data
public class BookingDtoRequest {
    @NotNull(groups = BookingOnCreate.class)
    private Long itemId;
    @NotNull(groups = BookingOnCreate.class)
    @FutureOrPresent(groups = BookingOnCreate.class)
    private LocalDateTime start;
    @NotNull(groups = BookingOnCreate.class)
    @Future(groups = BookingOnCreate.class)
    private LocalDateTime end;
}
