package ru.practicum.shareit.item.errors;

public class IncorrectUserException extends RuntimeException{
    public IncorrectUserException(String message) {
        super(message);
    }
}
