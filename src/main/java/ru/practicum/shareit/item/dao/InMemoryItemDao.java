package ru.practicum.shareit.item.dao;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.item.errors.ItemNotFoundException;
import ru.practicum.shareit.item.model.Item;

import java.util.*;
import java.util.stream.Collectors;

@Repository
@Slf4j
public class InMemoryItemDao implements ItemDao {
    private final Map<Long, Item> itemMap = new HashMap<>();
    private long id = 0;

    @Override
    public Item create(Item item) {
        item.setId(getId());
        itemMap.put(item.getId(), item);
        log.info("Добавлена вещь с id {}", item.getId());
        return item;
    }

    @Override
    public Item get(long itemId) {
        Optional<Item> item = Optional.ofNullable(itemMap.get(itemId));
        if (item.isPresent()) {
            log.info("Вещь с id {}", itemId);
            return item.get();
        } else {
            log.info("Вещь с id {} не найдена", itemId);
            throw new ItemNotFoundException(String.format("Вещь с id %s не найдена", itemId));
        }
    }

    @Override
    public Item update(Item item) {
        itemMap.put(item.getId(), item);
        log.info("Пользователь с id {} обновлён", item.getId());
        return item;
    }

    @Override
    public List<Item> getByOwner(long userId) {
        return itemMap.values().stream()
                .filter(i -> i.getOwner().getId() == userId)
                .collect(Collectors.toList());
    }

    @Override
    public List<Item> search(String text) {
        return itemMap.values().stream()
                .filter(Item::getAvailable)
                .filter(i -> i.getName().toLowerCase().contains(text.toLowerCase())
                        || i.getDescription().toLowerCase().contains(text.toLowerCase()))
                .collect(Collectors.toList());
    }

    private long getId() {
        log.info("Получен запрос на получение нового id");
        return ++id;
    }
}
