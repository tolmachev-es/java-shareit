package ru.practicum.shareit.item.controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.mappers.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.model.validationGroups.ItemOnCreate;
import ru.practicum.shareit.item.model.validationGroups.ItemOnUpdate;
import ru.practicum.shareit.item.service.ItemService;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


/**
 * TODO Sprint add-controllers.
 */
@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
@Slf4j
public class ItemController {
    private final ItemService itemService;

    @PostMapping
    public ItemDto create(@NotNull @RequestHeader("X-Sharer-User-Id") long userId,
                          @Validated(ItemOnCreate.class) @RequestBody Item item) {
        log.info("Получен запрос на создание вещи");
        return ItemMapper.ITEM_MAPPER.toDto(itemService.create(item, userId));
    }

    @PatchMapping("/{itemId}")
    public ItemDto update(@NotNull @RequestHeader("X-Sharer-User-Id") long userId,
                          @Validated(ItemOnUpdate.class) @RequestBody Item item,
                          @PathVariable long itemId) {
        log.info("Получен запрос на обновление вещи с id {} от пользователя {}", itemId, userId);
        return ItemMapper.ITEM_MAPPER.toDto(itemService.update(item, userId, itemId));
    }

    @GetMapping("/{itemId}")
    public ItemDto get(@PathVariable long itemId) {
        log.info("Получен запрос на получение вещи с id {]", itemId);
        return ItemMapper.ITEM_MAPPER.toDto(itemService.get(itemId));
    }

    @GetMapping
    public List<ItemDto> getAll(@NotNull @RequestHeader("X-Sharer-User-Id") Long userId) {
        log.info("Получен запрос на получение всех вещей пользователя {}", userId);
        return itemService.getAllByOwner(userId).stream()
                .map(ItemMapper.ITEM_MAPPER::toDto)
                .collect(Collectors.toList());
    }

    @GetMapping("/search")
    public List<ItemDto> search(@RequestParam String text) {
        if (text.isBlank()) {
            log.info("Получен запрос на поиск с пустым значением в запросе");
            return new ArrayList<>();
        } else {
            log.info("Получен запрос на поиск с текстом {}", text);
            return itemService.search(text).stream()
                    .map(ItemMapper.ITEM_MAPPER::toDto)
                    .collect(Collectors.toList());
        }
    }
}
