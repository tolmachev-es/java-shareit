package ru.practicum.shareit.user.dao;


import java.util.Set;

public interface UserDao {
    UserEntity create(UserEntity user);

    UserEntity update(UserEntity user, long id);

    Set<UserEntity> getAll();

    UserEntity getUserById(long id);

    void deleteById(long id);
}
