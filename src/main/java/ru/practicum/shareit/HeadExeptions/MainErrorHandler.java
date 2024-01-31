package ru.practicum.shareit.HeadExeptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import ru.practicum.shareit.booking.errors.ItemNotAvailable;
import ru.practicum.shareit.request.exception.IncorrectPaginationParameters;

import java.util.Map;

@ControllerAdvice
public class MainErrorHandler {

    @ExceptionHandler(IncorrectPaginationParameters.class)
    public ResponseEntity<Map<String, String>> handle(final IncorrectPaginationParameters incorrectPaginationParameters) {
        return new ResponseEntity<>(Map.of("Pagination",
                incorrectPaginationParameters.getMessage()), HttpStatus.BAD_REQUEST);
    }
}
