package ru.practicum.shareit.item.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.service.ItemService;

import java.util.Set;

@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
@Slf4j
@Validated
public class ItemController {
    private final ItemService itemService;

    @PostMapping
    @Operation(summary = "Создание вещи")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Item create"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    public ItemDto create(@RequestHeader("X-Sharer-User-Id") long userId,
                          @RequestBody ItemDto item) {
        log.info("Получен запрос на создание вещи");
        return itemService.create(item, userId);
    }

    @PatchMapping("/{itemId}")
    @Operation(summary = "Обновление вещи")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Item update"),
            @ApiResponse(responseCode = "403", description = "Insufficient rights"),
            @ApiResponse(responseCode = "404", description = "Item not found")
    })
    public ItemDto update(@RequestHeader("X-Sharer-User-Id") long userId,
                          @RequestBody ItemDto item,
                          @PathVariable long itemId) {
        log.info("Получен запрос на обновление вещи с id {} от пользователя {}", itemId, userId);
        return itemService.update(item, userId, itemId);
    }

    @GetMapping("/{itemId}")
    @Operation(summary = "Получение вещи по id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Item get"),
            @ApiResponse(responseCode = "404", description = "Item not found")
    })
    public ItemDto get(@PathVariable long itemId,
                       @RequestHeader("X-Sharer-User-Id") Long userId) {
        log.info("Получен запрос на получение вещи с id {}", itemId);
        return itemService.get(itemId, userId);
    }

    @GetMapping
    @Operation(summary = "Получение всех вещей пользователя по userId")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Items get"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    public Set<ItemDto> getAll(@RequestHeader("X-Sharer-User-Id") Long userId,
                               @RequestParam Integer from,
                               @RequestParam Integer size) {
        log.info("Получен запрос на получение всех вещей пользователя {}", userId);
        return itemService.getAllByOwner(userId, from, size);
    }

    @GetMapping("/search")
    @Operation(summary = "Поиск по вещам")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Items found")
    })
    public Set<ItemDto> search(@RequestParam String text,
                               @RequestParam Integer from,
                               @RequestParam Integer size) {
        log.info("Получен запрос на поиск с текстом {}", text);
        return itemService.search(text, from, size);
    }

    @PostMapping("/{itemId}/comment")
    @Operation(summary = "Поиск по вещам")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Comment successful append"),
            @ApiResponse(responseCode = "404", description = "Not found")
    })
    public CommentDto addComment(@RequestHeader("X-Sharer-User-Id") Long userId,
                                 @PathVariable long itemId,
                                 @RequestBody CommentDto commentDto) {
        log.info("Получен запрос на добавления комментария к вещи с id {}", itemId);
        return itemService.addComment(userId, itemId, commentDto);
    }
}
