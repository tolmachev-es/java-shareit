package ru.practicum.shareit.user.service;


import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.dto.UserDto;

import java.util.List;

public interface UserService {
    UserDto create(User user);
    UserDto update(User user, Long id);
    UserDto getById(Long id);
    List<UserDto> getAll();
    void deleteById(Long id);
}
