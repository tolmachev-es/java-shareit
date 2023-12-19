package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.model.Item;

public interface ItemService {
    Item create(Item item, long userId);

    Item update(Item item, long userId, long itemId);
}
