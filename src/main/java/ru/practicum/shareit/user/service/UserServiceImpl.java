package ru.practicum.shareit.user.service;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.dao.UserDao;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    @Qualifier("inMemoryUserDao")
    @NonNull
    private final UserDao userDao;

    @Override
    public User create(User user) {
        return userDao.create(user);
    }

    @Override
    public User update(User user, Long id) {
        return userDao.update(user, id);
    }

    @Override
    public User getById(Long id) {
        return userDao.getUserById(id);
    }

    @Override
    public List<User> getAll() {
        return userDao.getAll();
    }

    @Override
    public void deleteById(Long id) {
        userDao.deleteById(id);
    }

}
