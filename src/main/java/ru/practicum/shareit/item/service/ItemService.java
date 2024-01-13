package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.dto.ItemDto;

import java.util.Set;

public interface ItemService {
    ItemDto create(ItemDto item, long userId);

    ItemDto update(ItemDto item, long userId, long itemId);

    ItemDto get(Long itemId);

    Set<ItemDto> getAllByOwner(Long userId);

    Set<ItemDto> search(String text);
}
