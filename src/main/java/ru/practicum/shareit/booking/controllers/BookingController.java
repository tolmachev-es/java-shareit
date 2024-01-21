package ru.practicum.shareit.booking.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
public class BookingController {
    public final BookingService bookingService;

    @PostMapping
    @Operation(summary = "Создание бронирования")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Booking create"),
            @ApiResponse(responseCode = "404", description = "Not found"),
            @ApiResponse(responseCode = "400", description = "Unsupported method")
    })
    public BookingDtoResponse create(@NotNull @RequestHeader("X-Sharer-User-Id") long userId,
                                     @Validated(BookingOnCreate.class) @RequestBody BookingDtoRequest bookingRequest) {
        log.info("Получен запрос на создание бронирования");
        return bookingService.create(bookingRequest, userId);
    }

    @PatchMapping("/{bookingId}")
    @Operation(summary = "Подтверждение бронирования")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Booking approved"),
            @ApiResponse(responseCode = "404", description = "Approved unsupported")
    })
    public BookingDtoResponse approve(@NotNull @PathVariable long bookingId,
                                      @NotNull @RequestHeader("X-Sharer-User-Id") long userId,
                                      @RequestParam boolean approved) {
        log.info("Получен запрос на подтверждение бронирования");
        return bookingService.approve(bookingId, userId, approved);
    }

    @GetMapping("/{bookingId}")
    @Operation(summary = "Получение бронирования")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Booking get"),
            @ApiResponse(responseCode = "404", description = "Booking not found")
    })
    public BookingDtoResponse get(@NotNull @PathVariable long bookingId,
                                  @NotNull @RequestHeader("X-Sharer-User-Id") long userId) {
        log.info("Получен запрос на получение бронирования с id {}", bookingId);
        return bookingService.get(bookingId, userId);
    }

    @GetMapping
    @Operation(summary = "Получение бронирований по букеру")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful get"),
            @ApiResponse(responseCode = "404", description = "Unsupported method")
    })
    public Set<BookingDtoResponse> getByBooker(@RequestParam(required = false, defaultValue = "ALL") String state,
                                               @NotNull @RequestHeader("X-Sharer-User-Id") long userId) {
        log.info("Получен запрос на получение всех бронирований пользователя {}", userId);
        return bookingService.getByBooker(state, userId);
    }

    @GetMapping("/owner")
    @Operation(summary = "Получение бронирований по владельцу")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful get"),
            @ApiResponse(responseCode = "404", description = "Unsupported method")
    })
    public Set<BookingDtoResponse> getByOwner(@RequestParam(required = false, defaultValue = "ALL") String state,
                                              @NotNull @RequestHeader("X-Sharer-User-Id") long userId) {
        log.info("Получен запрос на получение бронирований по владельцу с id {}", userId);
        return bookingService.getByOwner(state, userId);
    }

}
