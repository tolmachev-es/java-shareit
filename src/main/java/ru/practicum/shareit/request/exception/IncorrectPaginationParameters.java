package ru.practicum.shareit.request.exception;

public class IncorrectPaginationParameters extends RuntimeException{
    public IncorrectPaginationParameters(String message) {
        super(message);
    }
}
