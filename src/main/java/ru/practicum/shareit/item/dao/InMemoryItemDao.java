package ru.practicum.shareit.item.dao;

import org.springframework.stereotype.Repository;
import ru.practicum.shareit.item.errors.ItemNotFoundException;
import ru.practicum.shareit.item.model.Item;

import java.util.*;
import java.util.stream.Collectors;

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

    @Override
    public Item update(Item item) {
        itemMap.put(item.getId(), item);
        return item;
    }

    @Override
    public List<Item> getByOwner(long userId) {
        return itemMap.values().stream()
                .filter(i -> i.getOwner().getId() == userId)
                .collect(Collectors.toList());
    }

    private long getId() {
        return ++id;
    }
}
