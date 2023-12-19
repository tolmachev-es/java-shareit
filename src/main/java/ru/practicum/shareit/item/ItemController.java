package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.model.validationGroups.OnCreate;
import ru.practicum.shareit.item.model.validationGroups.OnUpdate;
import ru.practicum.shareit.item.service.ItemService;


/**
 * TODO Sprint add-controllers.
 */
@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
public class ItemController {
    private final ItemService itemService;

    @PostMapping
    public ItemDto create(@RequestHeader("X-Sharer-User-Id") long userId,
                          @Validated(OnCreate.class) @RequestBody Item item) {
        return ItemMapper.ITEM_MAPPER.toDto(itemService.create(item, userId));
    }

    @PatchMapping("/{itemId}")
    public ItemDto update(@RequestHeader("X-Sharer-User-Id") long userId,
                          @Validated(OnUpdate.class) @RequestBody Item item,
                          @PathVariable long itemId) {
        return null;
    }
}
