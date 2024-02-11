package ru.practicum.shareit.HeadExeptions.handler;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import ru.practicum.shareit.HeadExeptions.AlreadyExistException;
import ru.practicum.shareit.HeadExeptions.ConflictException;
import ru.practicum.shareit.HeadExeptions.InvalidParameterException;
import ru.practicum.shareit.HeadExeptions.ObjectNotFound;

import javax.validation.ValidationException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ControllerAdvice
public class MainErrorHandler {

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

    @ExceptionHandler(ObjectNotFound.class)
    public ResponseEntity<Map<String, String>> handle(final ObjectNotFound objectNotFound) {
        return new ResponseEntity<>(Map.of("Not Found",
                objectNotFound.getMessage()),
                HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(AlreadyExistException.class)
    public ResponseEntity<Map<String, String>> handle(final AlreadyExistException alreadyExistException) {
        return new ResponseEntity<>(Map.of("Already exist",
                alreadyExistException.getMessage()),
                HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(InvalidParameterException.class)
    public ResponseEntity<Map<String, String>> handle(final InvalidParameterException invalidParameterException) {
        return new ResponseEntity<>(Map.of("error",
                invalidParameterException.getMessage()),
                HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ConflictException.class)
    public ResponseEntity<Map<String, String>> handle(final ConflictException conflictException) {
        return new ResponseEntity<>(Map.of("error",
                conflictException.getMessage()),
                HttpStatus.CONFLICT);
    }
}
