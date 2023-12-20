package ru.practicum.shareit.user.dao;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.errors.EmailAlreadyExistException;
import ru.practicum.shareit.user.errors.UserNotFoundException;

import java.util.*;

@Repository
@Slf4j
public class InMemoryUserDao implements UserDao {
    private final Map<Long, User> userMap = new HashMap<>();
    private long id = 0;

    @Override
    public User create(User user) {
        if(getUserByEmail(user.getEmail()).isEmpty()) {
            user.setId(getId());
            userMap.put(user.getId(), user);
            log.info("Добавлен пользователь с id {}", id);
            return user;
        } else {
            log.info("Пользователь с таким email уже существует. новый пользователь не добавлен");
            throw new EmailAlreadyExistException(String.format("Пользователь с Email %s уже есть в системе",
                    user.getEmail()));
        }
    }

    @Override
    public User update(User user, long id) {
        User oldUser = getUserById(id);
        if(user.getEmail() != null) {
            Optional<User> userWithSameEmail = getUserByEmail(user.getEmail());
            if(userWithSameEmail.isPresent() && userWithSameEmail.get().getId() != id) {
                log.info("Пользователь с таким email уже существует. Email не обновлен");
                throw new EmailAlreadyExistException(String.format("Пользователь с Email %s уже есть в системе",
                        user.getEmail()));
            } else {
                oldUser.setEmail(user.getEmail());
                log.info("Изменен email для пользователя");
            }
        }
        oldUser.setName(user.getName() == null ? oldUser.getName() : user.getName());
        log.info("Обновлены параметры пользователя");
        return oldUser;
    }

    @Override
    public List<User> getAll() {
        return new ArrayList<>(userMap.values());
    }

    @Override
    public User getUserById(long id) {
        if (userMap.containsKey(id)) {
            log.info("Началось получение пользователя с id {}", id);
            return userMap.get(id);
        } else {
            log.info("Пользователь с {} не найден", id);
            throw new UserNotFoundException(String.format("Пользователь с id %s не найден", id));
        }
    }

    @Override
    public void deleteById(long id) {
        getUserById(id);
        userMap.remove(id);
    }

    public Optional<User> getUserByEmail(String email) {
        return userMap.values().stream()
                .filter(u -> u.getEmail().equals(email))
                .findAny();
    }

    private long getId() {
        log.info("Получен запрос за получение нового id");
        return ++id;
    }
}
