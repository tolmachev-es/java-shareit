package ru.practicum.shareit.user.service;


import ru.practicum.shareit.user.dto.UserDto;

import java.util.Set;

public interface UserService {
    UserDto create(UserDto user);

    UserDto update(UserDto user, Long id);

    UserDto getById(Long id);

    Set<UserDto> getAll();

    void deleteById(Long id);
}
