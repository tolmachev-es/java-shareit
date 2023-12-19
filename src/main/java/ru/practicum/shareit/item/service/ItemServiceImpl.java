package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.item.dao.ItemDao;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.dao.UserDao;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {
    @Qualifier("inMemoryItemDao")
    private final ItemDao itemDao;
    @Qualifier("inMemoryUserDao")
    private final UserDao userDao;

    @Override
    public Item create(Item item, long userId) {
        item.setOwner(userDao.getUserById(userId));
        item.setAvailable(true);
        return itemDao.create(item);
    }

    @Override
    public Item update(Item item, long userId, long itemId) {
        Item oldItem = itemDao.get(userId);
        if (oldItem.getOwner().getId() == userId) {

        } else {

        }
        return null;
    }

}
