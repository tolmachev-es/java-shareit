package ru.practicum.shareit.HeadExeptions;

public class ObjectNotFound extends RuntimeException {
    public ObjectNotFound(String message) {
        super(message);
    }
}
