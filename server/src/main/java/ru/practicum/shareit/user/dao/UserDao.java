package ru.practicum.shareit.user.dao;


import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface UserDao {
    UserEntity create(UserEntity user);

    UserEntity update(UserEntity user, long id);

    Set<UserEntity> getAll();

    UserEntity getUserById(long id);

    void deleteById(long id);
}
