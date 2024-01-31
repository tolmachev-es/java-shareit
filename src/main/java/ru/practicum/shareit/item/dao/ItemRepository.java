package ru.practicum.shareit.item.dao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.shareit.user.dao.UserEntity;

import java.util.Optional;
import java.util.Set;

public interface ItemRepository extends JpaRepository<ItemEntity, Long> {
    Optional<ItemEntity> getItemEntityById(long id);

    Page<ItemEntity> getItemEntityByOwner(UserEntity owner, Pageable pageable);

    Page<ItemEntity> getItemEntityByDescriptionContainsIgnoreCaseAndAvailableIsTrueOrNameContainsIgnoreCaseAndAvailableIsTrue(
            String text, String text1, Pageable pageable);

    Set<ItemEntity> getItemEntitiesByItemRequestNotNull();

    Set<ItemEntity> getItemEntitiesByItemRequest_Id(Long itemRequest_id);
}
