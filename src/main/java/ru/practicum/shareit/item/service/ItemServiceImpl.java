package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.item.dao.ItemDao;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.errors.IncorrectUserException;
import ru.practicum.shareit.item.mappers.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.dao.UserDao;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {
    @Qualifier("inMemoryItemDao")
    private final ItemDao itemDao;
    @Qualifier("inMemoryUserDao")
    private final UserDao userDao;

    @Override
    public ItemDto create(ItemDto item, long userId) {
        Item newItem = ItemMapper.ITEM_MAPPER.fromDto(item);
        newItem.setOwner(userDao.getUserById(userId));
        return ItemMapper.ITEM_MAPPER.toDto(itemDao.create(newItem));
    }

    @Override
    public ItemDto update(ItemDto item, long userId, long itemId) {
        Item oldItem = itemDao.get(itemId);
        if (oldItem.getOwner().getId() == userId) {
            oldItem.setName(item.getName() != null ? item.getName() : oldItem.getName());
            oldItem.setDescription(item.getDescription() != null ? item.getDescription() : oldItem.getDescription());
            oldItem.setAvailable(item.getAvailable() != null ? item.getAvailable() : oldItem.getAvailable());
            return ItemMapper.ITEM_MAPPER.toDto(itemDao.update(oldItem));
        } else {
            throw new IncorrectUserException(
                    String.format("Пользователь %s не не имеет прав для редактирования вещи %s", userId, itemId));
        }
    }

    @Override
    public ItemDto get(Long itemId) {
        return ItemMapper.ITEM_MAPPER.toDto(itemDao.get(itemId));
    }

    @Override
    public List<ItemDto> getAllByOwner(Long userId) {
        userDao.getUserById(userId);
        return itemDao.getByOwner(userId).stream()
                .map(ItemMapper.ITEM_MAPPER::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<ItemDto> search(String text) {
        return itemDao.search(text).stream()
                .map(ItemMapper.ITEM_MAPPER::toDto)
                .collect(Collectors.toList());
    }

}
