package ru.practicum.shareit.user.service;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.user.dao.UserDao;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.mappers.UserMapper;
import ru.practicum.shareit.user.model.User;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    @Qualifier("inMemoryUserDao")
    @NonNull
    private final UserDao userDao;

    @Override
    public UserDto create(UserDto user) {
        User newUser = UserMapper.USER_MAPPER.fromDto(user);
        return UserMapper.USER_MAPPER
                .toDto(userDao.create(newUser));
    }

    @Override
    public UserDto update(UserDto user, Long id) {
        User updateUser = UserMapper.USER_MAPPER.fromDto(user);
        return UserMapper.USER_MAPPER
                .toDto(userDao.update(updateUser, id));
    }

    @Override
    public UserDto getById(Long id) {
        return UserMapper.USER_MAPPER
                .toDto(userDao.getUserById(id));
    }

    @Override
    public List<UserDto> getAll() {
        return userDao.getAll().stream()
                .map(UserMapper.USER_MAPPER::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteById(Long id) {
        userDao.deleteById(id);
    }

}
