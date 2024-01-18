package ru.practicum.shareit.booking.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import ru.practicum.shareit.booking.errors.*;

import javax.validation.ValidationException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ControllerAdvice
public class BookingErrorHandler {
    @ExceptionHandler({MethodArgumentNotValidException.class, ValidationException.class})
    public ResponseEntity<Map<String, String>> handle(final Exception ex) {
        Map<String, String> error = new HashMap<>();
        if (ex instanceof MethodArgumentNotValidException) {
            BindingResult bindingResult = ((MethodArgumentNotValidException) ex).getBindingResult();
            List<FieldError> errors = bindingResult.getFieldErrors();
            for (FieldError fe : errors) {
                error.put(fe.getField(), fe.getDefaultMessage());
            }
        }
        return ResponseEntity.badRequest().body(error);
    }

    @ExceptionHandler(ItemNotAvailable.class)
    public ResponseEntity<Map<String, String>> handle(final ItemNotAvailable itemNotAvailable) {
        return new ResponseEntity<>(Map.of("Item", itemNotAvailable.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(IncorrectBookingTime.class)
    public ResponseEntity<Map<String, String>> handle(final IncorrectBookingTime incorrectBookingTime) {
        return new ResponseEntity<>(Map.of("start date or end date", incorrectBookingTime.getMessage()),
                HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(UnsupportedMethod.class)
    public ResponseEntity<Map<String, String>> handle(final UnsupportedMethod unsupportedMethod) {
        return new ResponseEntity<>(Map.of("error", unsupportedMethod.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(IncorrectBookingRequest.class)
    public ResponseEntity<Map<String, String>> handle(final IncorrectBookingRequest incorrectBookingRequest) {
        return new ResponseEntity<>(Map.of("User", incorrectBookingRequest.getMessage()),
                HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(BookingNotFound.class)
    public ResponseEntity<Map<String, String>> handle(final BookingNotFound notFound) {
        return new ResponseEntity<>(Map.of("Booking", notFound.getMessage()),
                HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(IncorrectBookingOwner.class)
    public ResponseEntity<Map<String, String>> handle(final IncorrectBookingOwner incorrectBookingOwner) {
        return new ResponseEntity<>(Map.of("Booking", incorrectBookingOwner.getMessage()),
                HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(AlreadyCompleted.class)
    public ResponseEntity<Map<String, String>> handle(final AlreadyCompleted alreadyCompleted) {
        return new ResponseEntity<>(Map.of("Status", alreadyCompleted.getMessage()),
                HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(NotFoundBookingByUser.class)
    public ResponseEntity<Map<String, String>> handle(final NotFoundBookingByUser notFoundBookingByUser) {
        return new ResponseEntity<>(Map.of("Booking", notFoundBookingByUser.getMessage()),
                HttpStatus.BAD_REQUEST);
    }
}
