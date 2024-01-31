package ru.practicum.shareit.HeadExeptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import ru.practicum.shareit.booking.errors.ItemNotAvailable;
import ru.practicum.shareit.request.exception.IncorrectPaginationParameters;
import ru.practicum.shareit.request.exception.ItemRequestNotFound;
import ru.practicum.shareit.request.model.ItemRequest;

import java.util.Map;

@ControllerAdvice
public class MainErrorHandler {

    @ExceptionHandler(IncorrectPaginationParameters.class)
    public ResponseEntity<Map<String, String>> handle(final IncorrectPaginationParameters incorrectPaginationParameters) {
        return new ResponseEntity<>(Map.of("Pagination",
                incorrectPaginationParameters.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ItemRequestNotFound.class)
    public ResponseEntity<Map<String, String>> handle(final ItemRequestNotFound itemRequestNotFound) {
        return new ResponseEntity<>(Map.of("Pagination",
                itemRequestNotFound.getMessage()), HttpStatus.NOT_FOUND);
    }
}
