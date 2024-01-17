package ru.practicum.shareit.booking.controllers;

import lombok.AllArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDtoRequest;
import ru.practicum.shareit.booking.dto.BookingDtoResponse;
import ru.practicum.shareit.booking.model.validationGroups.BookingOnCreate;
import ru.practicum.shareit.booking.services.BookingService;

import javax.validation.constraints.NotNull;
import java.util.Set;

/**
 * TODO Sprint add-bookings.
 */
@RestController
@RequestMapping(path = "/bookings")
@AllArgsConstructor
public class BookingController {
    public final BookingService bookingService;

    @PostMapping
    public BookingDtoResponse create(@NotNull @RequestHeader("X-Sharer-User-Id") long userId,
                                     @Validated(BookingOnCreate.class) @RequestBody BookingDtoRequest bookingRequest) {
        return bookingService.create(bookingRequest, userId);
    }

    @PatchMapping("/{bookingId}")
    public BookingDtoResponse approve(@NotNull @PathVariable long bookingId,
                                      @NotNull @RequestHeader("X-Sharer-User-Id") long userId,
                                      @RequestParam boolean approved) {
        return bookingService.approve(bookingId, userId, approved);
    }

    @GetMapping("/{bookingId}")
    public BookingDtoResponse get(@NotNull @PathVariable long bookingId,
                                  @NotNull @RequestHeader("X-Sharer-User-Id") long userId) {
        return bookingService.get(bookingId, userId);
    }

    @GetMapping
    public Set<BookingDtoResponse> getByBooker(@RequestParam(required = false, defaultValue = "ALL") String state,
                                               @NotNull @RequestHeader("X-Sharer-User-Id") long userId) {
        return bookingService.getByBooker(state, userId);
    }

    @GetMapping("/owner")
    public Set<BookingDtoResponse> getByOwner(@RequestParam(required = false, defaultValue = "ALL") String state,
                                              @NotNull @RequestHeader("X-Sharer-User-Id") long userId) {
        return bookingService.getByOwner(state, userId);
    }

}
