package ru.practicum.shareit.user.service;


import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.dto.UserDto;

import java.util.List;

public interface UserService {
    User create(User user);
    User update(User user, Long id);
    User getById(Long id);
    List<User> getAll();
    void deleteById(Long id);
}
