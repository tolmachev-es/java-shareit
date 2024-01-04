package ru.practicum.shareit.user.dao;

import ru.practicum.shareit.user.model.User;

import java.util.List;

public interface UserDao {
    User create(User user);

    User update(User user, long id);

    List<User> getAll();

    User getUserById(long id);

    void deleteById(long id);
}
