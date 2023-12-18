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

import java.util.List;
import java.util.stream.Collectors;

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

    @Override
    public UserDto update(User user, Long id) {
        return UserMapper.toUserDto(userDao.update(user, id));
    }

    @Override
    public UserDto getById(Long id) {
        return UserMapper.toUserDto(userDao.getUserById(id));
    }

    @Override
    public List<UserDto> getAll() {
        return userDao.getAll().stream()
                .map(UserMapper::toUserDto)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteById(Long id) {
        userDao.deleteById(id);
    }

}
