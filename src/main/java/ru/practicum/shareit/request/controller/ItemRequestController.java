package ru.practicum.shareit.request.controller;

import io.swagger.v3.oas.annotations.Operation;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.service.ItemRequestService;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.Set;


@RestController
@RequestMapping(path = "/requests")
@AllArgsConstructor
@Slf4j
public class ItemRequestController {
    public final ItemRequestService itemRequestService;

    @PostMapping
    @Operation(summary = "Добавление запроса на вещь")
    public ItemRequestDto create(@NotNull @RequestHeader("X-Sharer-User-Id") Long userId,
                                 @Valid @RequestBody ItemRequestDto itemRequestDto) {
        return itemRequestService.create(itemRequestDto, userId);
    }

    @GetMapping
    @Operation(summary = "Получение запроса")
    public Set<ItemRequestDto> get(@NotNull @RequestHeader("X-Sharer-User-Id") Long userId) {
        return itemRequestService.get(userId);
    }

    @GetMapping("/all")
    public Set<ItemRequestDto> getWithPagination(@NotNull @RequestHeader("X-Sharer-User-Id") Long userId,
                                                 @RequestParam(defaultValue = "0") Integer from,
                                                 @RequestParam(defaultValue = "10") Integer size) {
        return itemRequestService.getWithPagination(userId, from, size);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Получение по id")
    public ItemRequestDto get(@NotNull @RequestHeader("X-Sharer-User-Id") Long userId,
                              @PathVariable Long id) {
        return itemRequestService.getById(id, userId);
    }
}
