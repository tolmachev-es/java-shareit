package ru.practicum.shareit.item.service;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.item.dao.ItemDao;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.mappers.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.dao.UserDao;
import ru.practicum.shareit.user.dao.UserEntity;
import ru.practicum.shareit.user.mappers.UserMapper;

import java.util.Set;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class ItemServiceImpl implements ItemService {
    @Qualifier("DBItemDao")
    private final ItemDao itemDao;
    @Qualifier("DBUserDao")
    private final UserDao userDao;

    @Override
    public ItemDto create(ItemDto item, long userId) {
        Item newItem = ItemMapper.ITEM_MAPPER.fromDto(item);
        newItem.setOwner(UserMapper.USER_MAPPER.fromEntity(userDao.getUserById(userId)));
        Item createItem = ItemMapper.ITEM_MAPPER.fromEntity(
                itemDao.create(ItemMapper.ITEM_MAPPER.toEntity(newItem)));
        return ItemMapper.ITEM_MAPPER.toDto(createItem);
    }

    @Override
    public ItemDto update(ItemDto item, long userId, long itemId) {
        Item itemToUpdate = ItemMapper.ITEM_MAPPER.fromDto(item);
        itemToUpdate.setOwner(UserMapper.USER_MAPPER.fromEntity(userDao.getUserById(userId)));
        itemToUpdate.setId(itemId);
        Item updateItem = ItemMapper.ITEM_MAPPER.fromEntity(
                itemDao.update(ItemMapper.ITEM_MAPPER.toEntity(itemToUpdate)));
        return ItemMapper.ITEM_MAPPER.toDto(updateItem);
    }

    @Override
    public ItemDto get(Long itemId) {
        Item item = ItemMapper.ITEM_MAPPER.fromEntity(itemDao.get(itemId));
        return ItemMapper.ITEM_MAPPER.toDto(item);
    }

    @Override
    public Set<ItemDto> getAllByOwner(Long userId) {
        UserEntity user = userDao.getUserById(userId);
        Set<Item> items = itemDao.getByOwner(user).stream()
                .map(ItemMapper.ITEM_MAPPER::fromEntity)
                .collect(Collectors.toSet());
        return items.stream()
                .map(ItemMapper.ITEM_MAPPER::toDto)
                .collect(Collectors.toSet());
    }

    @Override
    public Set<ItemDto> search(String text) {
        Set<Item> items = itemDao.search(text).stream()
                .map(ItemMapper.ITEM_MAPPER::fromEntity)
                .collect(Collectors.toSet());
        return items.stream()
                .map(ItemMapper.ITEM_MAPPER::toDto)
                .collect(Collectors.toSet());
    }
}
