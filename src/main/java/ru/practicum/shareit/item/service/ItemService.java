package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemService {
    Item create(Item item, long userId);
    Item update(Item item, long userId, long itemId);
    Item get(Long itemId);
    List<Item> getAllByOwner(Long userId);
    List<Item> search(String text);
}
