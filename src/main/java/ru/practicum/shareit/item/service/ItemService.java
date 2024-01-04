package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.dto.ItemDto;

import java.util.List;

public interface ItemService {
    ItemDto create(ItemDto item, long userId);

    ItemDto update(ItemDto item, long userId, long itemId);

    ItemDto get(Long itemId);

    List<ItemDto> getAllByOwner(Long userId);

    List<ItemDto> search(String text);
}
