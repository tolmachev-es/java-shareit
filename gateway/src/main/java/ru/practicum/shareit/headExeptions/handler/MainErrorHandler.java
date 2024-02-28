package ru.practicum.shareit.headExeptions.handler;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import ru.practicum.shareit.headExeptions.InvalidParameterException;

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

    @ExceptionHandler(InvalidParameterException.class)
    public ResponseEntity<Map<String, String>> handle(final InvalidParameterException invalidParameterException) {
        return new ResponseEntity<>(Map.of("error",
                invalidParameterException.getMessage()),
                HttpStatus.BAD_REQUEST);
    }
}
