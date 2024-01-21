package ru.practicum.shareit.item.dao;

import ru.practicum.shareit.user.dao.UserEntity;

import java.util.Set;

public interface ItemDao {
    ItemEntity create(ItemEntity item);

    ItemEntity get(long itemId);

    ItemEntity update(ItemEntity item);

    Set<ItemEntity> getByOwner(UserEntity user);

    Set<ItemEntity> search(String text);
}
