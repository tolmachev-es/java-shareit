package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.item.dao.ItemDao;
import ru.practicum.shareit.item.errors.IncorrectUserException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.dao.UserDao;

import java.util.List;

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
        return itemDao.create(item);
    }

    @Override
    public Item update(Item item, long userId, long itemId) {
        Item oldItem = itemDao.get(itemId);
        if (oldItem.getOwner().getId() == userId) {
            oldItem.setName(item.getName() != null ? item.getName() : oldItem.getName());
            oldItem.setDescription(item.getDescription() != null ? item.getDescription() : oldItem.getDescription());
            oldItem.setAvailable(item.getAvailable() != null ? item.getAvailable() : oldItem.getAvailable());
            return itemDao.update(oldItem);
        } else {
            throw new IncorrectUserException(
                    String.format("Пользователь %s не не имеет прав для редактирования вещи %s", userId, itemId));
        }
    }

    @Override
    public Item get(Long itemId) {
        return itemDao.get(itemId);
    }

    @Override
    public List<Item> getAllByOwner(Long userId) {
        return itemDao.getByOwner(userId);
    }

    @Override
    public List<Item> search(String text) {
        return itemDao.search(text);
    }

}
