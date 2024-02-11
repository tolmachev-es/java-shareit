package ru.practicum.shareit.HeadExeptions;

public class InvalidParameterException extends RuntimeException {
    public InvalidParameterException(String message) {
        super(message);
    }
}
