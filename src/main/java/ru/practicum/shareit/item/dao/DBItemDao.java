package ru.practicum.shareit.item.dao;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.HeadExeptions.InvalidParameterException;
import ru.practicum.shareit.HeadExeptions.ObjectNotFound;
import ru.practicum.shareit.user.dao.UserEntity;

import javax.annotation.Resource;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

@Component
@Slf4j
@AllArgsConstructor
public class DBItemDao implements ItemDao {
    @Resource
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
            throw new ObjectNotFound(String.format("Вещь с id %s не найдена", itemId));
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
            throw new InvalidParameterException(
                    String.format("Пользователь %s не не имеет прав для редактирования вещи %s",
                            item.getOwner().getId(),
                            item.getId()));
        }
    }

    @Override
    public Set<ItemEntity> getByOwner(UserEntity user, Pageable pageable) {
        return itemRepository.getItemEntityByOwner(user, pageable).toSet();
    }

    @Override
    public Set<ItemEntity> search(String text, Pageable pageable) {
        return itemRepository
                .getItemEntityByDescriptionContainsIgnoreCaseAndAvailableIsTrueOrNameContainsIgnoreCaseAndAvailableIsTrue(
                        text.toLowerCase(), text.toLowerCase(), pageable).toSet();
    }
}
