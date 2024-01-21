package ru.practicum.shareit.item.dao;

import java.util.Set;

public interface CommentDao {
    CommentEntity create(CommentEntity commentEntity);

    Set<CommentEntity> getAllByItem(Long itemId);
}
