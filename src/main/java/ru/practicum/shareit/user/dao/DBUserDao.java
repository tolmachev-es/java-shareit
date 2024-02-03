package ru.practicum.shareit.user.dao;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.HeadExeptions.ConflictException;
import ru.practicum.shareit.HeadExeptions.ObjectNotFound;

import java.util.Optional;
import java.util.Set;

@Component
@Slf4j
@AllArgsConstructor
public class DBUserDao implements UserDao {

    private final UserRepository userRepository;

    @Override
    public UserEntity create(UserEntity user) {
        try {
            Long id = userRepository.save(user).getId();
            log.info("Добавлен пользователь с id {}", id);
            user.setId(id);
            return user;
        } catch (DataIntegrityViolationException e) {
            log.info("Пользователь с таким email уже существует. новый пользователь не добавлен");
            throw new ConflictException(String.format("Пользователь с Email %s уже есть в системе",
                    user.getEmail()));
        }
    }

    @Override
    public UserEntity update(UserEntity user, long id) {
        UserEntity oldUser = getUserById(id);
        if (user.getEmail() != null) {
            log.info("Будет обновлен email для пользователя {}", id);
            oldUser.setEmail(user.getEmail());
        }
        if (user.getName() != null) {
            log.info("Будет обновлен name для пользователя {}", id);
            oldUser.setName(user.getName());
        }
        try {
            userRepository.save(oldUser);
            log.info("Обновлены паарметры пользователя {}", id);
        } catch (DataIntegrityViolationException e) {
            log.info("Пользователь с таким email уже существует. новый пользователь не добавлен");
            throw new ConflictException(String.format("Пользователь с Email %s уже есть в системе",
                    user.getEmail()));
        }
        return oldUser;
    }

    @Override
    public Set<UserEntity> getAll() {
        return userRepository.findAllByOrderByIdAsc();
    }

    @Override
    public UserEntity getUserById(long id) {
        Optional<UserEntity> user = userRepository.getUserById(id);
        if (user.isEmpty()) {
            log.info("Пользователь с {} не найден", id);
            throw new ObjectNotFound(String.format("Пользователь с id %s не найден", id));
        } else {
            log.info("Началось получение пользователя с id {}", id);
            return user.get();
        }
    }

    @Override
    public void deleteById(long id) {
        userRepository.deleteById(id);
        log.info("Удален пользователь с id {}", id);
    }
}
