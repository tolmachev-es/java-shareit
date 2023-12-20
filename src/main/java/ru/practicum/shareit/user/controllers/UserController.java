package ru.practicum.shareit.user.controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.mappers.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.model.validationGroups.UserOnCreate;
import ru.practicum.shareit.user.model.validationGroups.UserOnUpdate;
import ru.practicum.shareit.user.service.UserService;

import java.util.List;
import java.util.stream.Collectors;

/**
 * TODO Sprint add-controllers.
 */
@RestController
@RequestMapping(path = "/users")
@RequiredArgsConstructor
@Slf4j
public class UserController {
    private final UserService userService;

    @PostMapping
    public UserDto create(@Validated(UserOnCreate.class) @RequestBody User user) {
        log.info("Получен запрос на добавление юзера");
        return UserMapper.USER_MAPPER.toDto(userService.create(user));
    }

    @PatchMapping("/{id}")
    public UserDto update(@Validated(UserOnUpdate.class) @RequestBody User user,
                          @PathVariable("id") Long id) {
        log.info("Получен запрос на обновление пользователя");
        return UserMapper.USER_MAPPER.toDto(userService.update(user, id));
    }

    @GetMapping("/{id}")
    public UserDto getById(@PathVariable("id") Long id) {
        log.info("Получен запрос на получение пользователя с id {}", id);
        return UserMapper.USER_MAPPER.toDto(userService.getById(id));
    }

    @GetMapping
    public List<UserDto> getAll() {
        log.info("Получен запрос на получение всех пользователей");
        return userService.getAll().stream()
                .map(UserMapper.USER_MAPPER::toDto)
                .collect(Collectors.toList());
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable("id") Long id) {
        log.info("Получен запрос на удаление удаление пользователя с id {}", id);
        userService.deleteById(id);
    }
}
