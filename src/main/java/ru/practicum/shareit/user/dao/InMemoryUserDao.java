package ru.practicum.shareit.user.dao;

import org.springframework.stereotype.Repository;
import ru.practicum.shareit.user.User;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Repository
public class InMemoryUserDao implements UserDao {
    private final Map<Long, User> userMap = new HashMap<>();
    private long id = 0;

    @Override
    public User create(User user) {
        if(getUserByEmail(user.getEmail()).isEmpty()) {
            user.setId(getId());
            userMap.put(user.getId(), user);
            return user;
        } else {
            throw new RuntimeException();
        }
    }

    public Optional<User> getUserByEmail(String email) {
        return userMap.values().stream()
                .filter(u -> u.getEmail().equals(email))
                .findAny();
    }

    private long getId() {
        return ++id;
    }
}
