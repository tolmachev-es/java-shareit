package ru.practicum.shareit.item.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.shareit.user.dao.UserEntity;

import java.util.Optional;
import java.util.Set;

public interface ItemRepository extends JpaRepository<ItemEntity, Long> {
    Optional<ItemEntity> getItemEntityById(long id);

    Set<ItemEntity> getItemEntityByOwner(UserEntity owner);

    Set<ItemEntity> getItemEntityByDescriptionContainsIgnoreCaseAndAvailableIsTrueOrNameContainsIgnoreCaseAndAvailableIsTrue(
            String text, String text1);
    Set<ItemEntity> getItemEntitiesByItemRequestNotNull();

    Set<ItemEntity> getItemEntitiesByItemRequest_Id(Long itemRequest_id);
}
