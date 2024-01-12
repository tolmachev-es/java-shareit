package ru.practicum.shareit.user.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.validationGroups.UserOnCreate;
import ru.practicum.shareit.user.model.validationGroups.UserOnUpdate;
import ru.practicum.shareit.user.service.UserService;

import java.util.Comparator;
import java.util.List;
import java.util.Set;
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
    @Operation(summary = "Создание пользователя")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User create"),
            @ApiResponse(responseCode = "409", description = "Email already exist")
    })
    public UserDto create(@Validated(UserOnCreate.class) @RequestBody UserDto user) {
        log.info("Получен запрос на добавление юзера");
        return userService.create(user);
    }

    @PatchMapping("/{id}")
    @Operation(summary = "Обновление пользователя")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User update"),
            @ApiResponse(responseCode = "409", description = "Email already exist")
    })
    public UserDto update(@Validated(UserOnUpdate.class) @RequestBody UserDto user,
                          @PathVariable("id") Long id) {
        log.info("Получен запрос на обновление пользователя");
        return userService.update(user, id);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Получение пользователя по id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User get"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    public UserDto getById(@PathVariable("id") Long id) {
        log.info("Получен запрос на получение пользователя с id {}", id);
        return userService.getById(id);
    }

    @GetMapping
    @Operation(summary = "Получение всех пользователей")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Users get")
    })
    public Set<UserDto> getAll() {
        log.info("Получен запрос на получение всех пользователей");
        return userService.getAll();
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Удаление пользователей по id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User delete"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    public void delete(@PathVariable("id") Long id) {
        log.info("Получен запрос на удаление удаление пользователя с id {}", id);
        userService.deleteById(id);
    }
}
