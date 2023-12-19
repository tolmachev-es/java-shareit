package ru.practicum.shareit.item.dao;

import org.springframework.stereotype.Repository;
import ru.practicum.shareit.item.model.Item;

import java.util.HashMap;
import java.util.Map;

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

    private long getId() {
        return ++id;
    }
}
