package ru.practicum.shareit.user.service;

import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserMapper;
import ru.practicum.shareit.user.dao.UserDao;
import ru.practicum.shareit.user.dto.UserDto;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    @Qualifier("inMemoryUserDao")
    @NonNull
    private final UserDao userDao;

    @Override
    public UserDto create(User user) {
        return UserMapper.toUserDto(userDao.create(user));
    }
}
