package ru.practicum.shareit.user.service;


import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.dto.UserDto;

public interface UserService {
    UserDto create(User user);
}
