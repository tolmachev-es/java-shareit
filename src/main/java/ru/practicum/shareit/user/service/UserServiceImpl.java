package ru.practicum.shareit.user.service;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.user.dao.UserDao;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.mappers.UserMapper;
import ru.practicum.shareit.user.model.User;

import java.util.Set;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {

    @Qualifier("DBUserDao")
    private final UserDao userDao;

    @Override
    public UserDto create(UserDto user) {
        User newUser = UserMapper.USER_MAPPER.fromDto(user);
        User createUser = UserMapper.USER_MAPPER.fromEntity(userDao.create(UserMapper.USER_MAPPER.toEntity(newUser)));
        return UserMapper.USER_MAPPER
                .toDto(createUser);
    }

    @Override
    public UserDto update(UserDto user, Long id) {
        User updateUser = UserMapper.USER_MAPPER.fromDto(user);
        User afterUpdateUser = UserMapper.USER_MAPPER.fromEntity(
                userDao.update(UserMapper.USER_MAPPER.toEntity(updateUser), id));
        return UserMapper.USER_MAPPER
                .toDto(afterUpdateUser);
    }

    @Override
    public UserDto getById(Long id) {
        User getUser = UserMapper.USER_MAPPER.fromEntity(userDao.getUserById(id));
        return UserMapper.USER_MAPPER
                .toDto(getUser);
    }

    @Override
    public Set<UserDto> getAll() {
        Set<User> allUsers = userDao.getAll().stream()
                .map(UserMapper.USER_MAPPER::fromEntity)
                .collect(Collectors.toSet());
        return allUsers.stream()
                .map(UserMapper.USER_MAPPER::toDto)
                .collect(Collectors.toSet());
    }

    @Override
    public void deleteById(Long id) {
        userDao.deleteById(id);
    }

}
