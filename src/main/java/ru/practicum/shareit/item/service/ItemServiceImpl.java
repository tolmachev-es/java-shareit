package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.item.ItemMapper;
import ru.practicum.shareit.item.dao.ItemDao;
import ru.practicum.shareit.item.dto.ItemDto;
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
    public ItemDto create(Item item, long userId) {
        item.setOwner(userDao.getUserById(userId));
        item.setAvailable(true);
        return ItemMapper.toItemDto(itemDao.create(item));
    }

}