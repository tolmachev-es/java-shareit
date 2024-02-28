package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.validationGroups.UserOnCreate;
import ru.practicum.shareit.user.dto.validationGroups.UserOnUpdate;


@Controller
@RequestMapping(path = "/users")
@RequiredArgsConstructor
@Slf4j
@Validated
public class UserController {
    private final UserClient userClient;

    @PostMapping
    public ResponseEntity<Object> create(@Validated(UserOnCreate.class) @RequestBody UserDto user) {
        log.info("Получен запрос на добавление юзера");
        return userClient.createUser(user);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Object> update(@Validated(UserOnUpdate.class) @RequestBody UserDto user,
                                         @PathVariable("id") Long id) {
        log.info("Получен запрос на обновление пользователя");
        return userClient.updateUser(id, user);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getById(@PathVariable("id") Long id) {
        log.info("Получен запрос на получение пользователя с id {}", id);
        return userClient.getUser(id);
    }

    @GetMapping
    public ResponseEntity<Object> getAll() {
        log.info("Получен запрос на получение всех пользователей");
        return userClient.getUsers();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> delete(@PathVariable("id") Long id) {
        log.info("Получен запрос на удаление удаление пользователя с id {}", id);
        return userClient.deleteUser(id);
    }
}
