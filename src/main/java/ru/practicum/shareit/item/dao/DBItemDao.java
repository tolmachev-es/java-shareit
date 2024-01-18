package ru.practicum.shareit.item.dao;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.item.errors.IncorrectUserException;
import ru.practicum.shareit.item.errors.ItemNotFoundException;
import ru.practicum.shareit.user.dao.UserEntity;

import java.util.Objects;
import java.util.Optional;
import java.util.Set;

@Repository
@Slf4j
@AllArgsConstructor
public class DBItemDao implements ItemDao {
    private final ItemRepository itemRepository;

    @Override
    public ItemEntity create(ItemEntity item) {
        itemRepository.save(item);
        return item;
    }

    @Override
    public ItemEntity get(long itemId) {
        Optional<ItemEntity> item = itemRepository.getItemEntityById(itemId);
        if (item.isPresent()) {
            log.info("Вещь с id {}", itemId);
            return item.get();
        } else {
            log.info("Вещь с id {} не найдена", itemId);
            throw new ItemNotFoundException(String.format("Вещь с id %s не найдена", itemId));
        }
    }

    @Override
    public ItemEntity update(ItemEntity item) {
        ItemEntity oldItem = get(item.getId());
        if (Objects.equals(oldItem.getOwner().getId(), item.getOwner().getId())) {
            oldItem.setName(item.getName() != null ? item.getName() : oldItem.getName());
            oldItem.setDescription(item.getDescription() != null ? item.getDescription() : oldItem.getDescription());
            oldItem.setAvailable(item.getAvailable() != null ? item.getAvailable() : oldItem.getAvailable());
            itemRepository.save(oldItem);
            return oldItem;
        } else {
            throw new IncorrectUserException(
                    String.format("Пользователь %s не не имеет прав для редактирования вещи %s",
                            item.getOwner().getId(),
                            item.getId()));
        }
    }

    @Override
    public Set<ItemEntity> getByOwner(UserEntity user) {
        return itemRepository.getItemEntityByOwner(user);
    }

    @Override
    public Set<ItemEntity> search(String text) {
        return itemRepository.
                getItemEntityByDescriptionContainsIgnoreCaseAndAvailableIsTrueOrNameContainsIgnoreCaseAndAvailableIsTrue(
                text.toLowerCase(), text.toLowerCase());
    }
}
