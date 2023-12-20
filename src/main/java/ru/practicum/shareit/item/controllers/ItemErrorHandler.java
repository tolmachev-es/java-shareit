package ru.practicum.shareit.item.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import ru.practicum.shareit.item.errors.IncorrectUserException;
import ru.practicum.shareit.item.errors.ItemNotFoundException;

import javax.validation.ValidationException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ControllerAdvice
public class ItemErrorHandler {

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

    @ExceptionHandler(IncorrectUserException.class)
    public ResponseEntity<Map<String, String>> handle(final IncorrectUserException exception) {
        return new ResponseEntity<>(Map.of("Item", exception.getMessage()), HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(ItemNotFoundException.class)
    public ResponseEntity<Map<String, String>> handle(final ItemNotFoundException exception) {
        return new ResponseEntity<>(Map.of("Item", exception.getMessage()), HttpStatus.NOT_FOUND);
    }
}
