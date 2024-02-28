package ru.practicum.shareit.item;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.validationGroups.ItemOnCreate;
import ru.practicum.shareit.item.dto.validationGroups.ItemOnUpdate;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@Controller
@RequestMapping(path = "/items")
@RequiredArgsConstructor
@Slf4j
@Validated
public class ItemController {
    private final ItemClient itemClient;

    @PostMapping
    public ResponseEntity<Object> create(@NotNull @RequestHeader("X-Sharer-User-Id") long userId,
                                         @Validated(ItemOnCreate.class) @RequestBody ItemDto item) {
        log.info("Получен запрос на создание вещи");
        return itemClient.createItem(userId, item);
    }

    @PatchMapping("/{itemId}")
    @Operation(summary = "Обновление вещи")
    public ResponseEntity<Object> update(@NotNull @RequestHeader("X-Sharer-User-Id") long userId,
                                         @Validated(ItemOnUpdate.class) @RequestBody ItemDto item,
                                         @PathVariable long itemId) {
        log.info("Получен запрос на обновление вещи с id {} от пользователя {}", itemId, userId);
        return itemClient.updateItem(itemId, userId, item);
    }

    @GetMapping("/{itemId}")
    public ResponseEntity<Object> get(@PathVariable long itemId,
                                      @NotNull @RequestHeader("X-Sharer-User-Id") Long userId) {
        log.info("Получен запрос на получение вещи с id {}", itemId);
        return itemClient.get(itemId, userId);
    }

    @GetMapping
    public ResponseEntity<Object> getAll(@NotNull @RequestHeader("X-Sharer-User-Id") Long userId,
                                         @RequestParam(defaultValue = "0") Integer from,
                                         @RequestParam(defaultValue = "10") Integer size) {
        log.info("Получен запрос на получение всех вещей пользователя {}", userId);
        return itemClient.getAllByOwner(userId, from, size);
    }

    @GetMapping("/search")
    public ResponseEntity<Object> search(@RequestParam String text,
                                         @NotNull @RequestHeader("X-Sharer-User-Id") Long userId,
                                         @RequestParam(defaultValue = "0") Integer from,
                                         @RequestParam(defaultValue = "10") Integer size) {
        log.info("Получен запрос на поиск с текстом {}", text);
        return itemClient.search(text, userId, from, size);
    }

    @PostMapping("/{itemId}/comment")
    public ResponseEntity<Object> addComment(@NotNull @RequestHeader("X-Sharer-User-Id") Long userId,
                                             @PathVariable long itemId,
                                             @Valid @RequestBody CommentDto commentDto) {
        log.info("Получен запрос на добавления комментария к вещи с id {}", itemId);
        return itemClient.addComment(userId, itemId, commentDto);
    }
}
