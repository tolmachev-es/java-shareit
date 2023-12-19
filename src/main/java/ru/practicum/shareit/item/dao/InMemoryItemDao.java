package ru.practicum.shareit.item.dao;

import org.springframework.stereotype.Repository;
import org.springframework.validation.annotation.Validated;
import ru.practicum.shareit.item.errors.ItemNotFoundException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.model.validationGroups.OnCreate;
import ru.practicum.shareit.item.model.validationGroups.OnUpdate;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Repository
public class InMemoryItemDao implements ItemDao {
    private final Map<Long, Item> itemMap = new HashMap<>();
    private long id = 0;

    @Override
    public Item create(Item item) {
        item.setId(getId());
        itemMap.put(item.getId(), item);
        return item;
    }

    @Override
    public Item get(long itemId) {
        Optional<Item> item = Optional.ofNullable(itemMap.get(itemId));
        if (item.isPresent()) {
            return item.get();
        } else {
            throw new ItemNotFoundException(String.format("Вещь с id %s не найдена", itemId));
        }
    }

    private long getId() {
        return ++id;
    }
}
