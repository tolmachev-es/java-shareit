package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.service.UserService;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

/**
 * TODO Sprint add-controllers.
 */
@RestController
@RequestMapping(path = "/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping
    public UserDto create(@Valid @RequestBody User user) {
        return UserMapper.USER_MAPPER.toDto(userService.create(user));
    }

    @PatchMapping("/{id}")
    public UserDto update(@RequestBody User user, @PathVariable("id") Long id) {
        return UserMapper.USER_MAPPER.toDto(userService.update(user, id));
    }

    @GetMapping("/{id}")
    public UserDto getById(@PathVariable("id") Long id) {
        return UserMapper.USER_MAPPER.toDto(userService.getById(id));
    }

    @GetMapping
    public List<UserDto> getAll() {
        return userService.getAll().stream()
                .map(UserMapper.USER_MAPPER::toDto)
                .collect(Collectors.toList());
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable("id") Long id) {
        userService.deleteById(id);
    }
}
