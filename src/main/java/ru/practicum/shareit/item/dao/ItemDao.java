package ru.practicum.shareit.item.dao;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.user.dao.UserEntity;

import java.util.Set;

@Repository
public interface ItemDao {
    ItemEntity create(ItemEntity item);

    ItemEntity get(long itemId);

    ItemEntity update(ItemEntity item);

    Set<ItemEntity> getByOwner(UserEntity user, Pageable pageable);

    Set<ItemEntity> search(String text, Pageable pageable);
}
