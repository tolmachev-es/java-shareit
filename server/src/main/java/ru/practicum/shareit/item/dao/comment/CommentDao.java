package ru.practicum.shareit.item.dao.comment;

import java.util.Set;

public interface CommentDao {
    CommentEntity create(CommentEntity commentEntity);

    Set<CommentEntity> getAllByItem(Long itemId);
}
