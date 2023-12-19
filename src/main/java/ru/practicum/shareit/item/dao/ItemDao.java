package ru.practicum.shareit.item.dao;

import ru.practicum.shareit.item.model.Item;

import java.util.Collection;
import java.util.List;

public interface ItemDao {
    Item create(Item item);
    Item get(long itemId);
    Item update(Item item);
    List<Item> getByOwner(long userId);
}
