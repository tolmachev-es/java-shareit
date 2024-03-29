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
import ru.practicum.shareit.booking.services.BookingService;

import java.util.Set;

/**
 * TODO Sprint add-bookings.
 */
@RestController
@RequestMapping(path = "/bookings")
@AllArgsConstructor
@Slf4j
@Validated
public class BookingController {
    public final BookingService bookingService;

    @PostMapping
    @Operation(summary = "Создание бронирования")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Booking create"),
            @ApiResponse(responseCode = "404", description = "Not found"),
            @ApiResponse(responseCode = "400", description = "Unsupported method")
    })
    public BookingDtoResponse create(@RequestHeader("X-Sharer-User-Id") long userId,
                                     @RequestBody BookingDtoRequest bookingRequest) {
        log.info("Получен запрос на создание бронирования");
        return bookingService.create(bookingRequest, userId);
    }

    @PatchMapping("/{bookingId}")
    @Operation(summary = "Подтверждение бронирования")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Booking approved"),
            @ApiResponse(responseCode = "404", description = "Approved unsupported")
    })
    public BookingDtoResponse approve(@PathVariable long bookingId,
                                      @RequestHeader("X-Sharer-User-Id") long userId,
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
    public BookingDtoResponse get(@PathVariable long bookingId,
                                  @RequestHeader("X-Sharer-User-Id") long userId) {
        log.info("Получен запрос на получение бронирования с id {}", bookingId);
        return bookingService.get(bookingId, userId);
    }

    @GetMapping
    @Operation(summary = "Получение бронирований по букеру")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful get"),
            @ApiResponse(responseCode = "404", description = "Unsupported method")
    })
    public Set<BookingDtoResponse> getByBooker(@RequestParam String state,
                                               @RequestHeader("X-Sharer-User-Id") long userId,
                                               @RequestParam Integer from,
                                               @RequestParam Integer size) {
        log.info("Получен запрос на получение всех бронирований пользователя {}", userId);
        return bookingService.getByBooker(state, userId, from, size);
    }

    @GetMapping("/owner")
    @Operation(summary = "Получение бронирований по владельцу")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful get"),
            @ApiResponse(responseCode = "404", description = "Unsupported method")
    })
    public Set<BookingDtoResponse> getByOwner(@RequestParam String state,
                                              @RequestHeader("X-Sharer-User-Id") long userId,
                                              @RequestParam Integer from,
                                              @RequestParam Integer size) {
        log.info("Получен запрос на получение бронирований по владельцу с id {}", userId);
        return bookingService.getByOwner(state, userId, from, size);
    }

}
