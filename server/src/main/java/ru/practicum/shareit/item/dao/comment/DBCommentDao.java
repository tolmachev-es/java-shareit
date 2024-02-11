package ru.practicum.shareit.item.dao.comment;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
@AllArgsConstructor
public class DBCommentDao implements CommentDao {
    private final CommentRepository commentRepository;

    @Override
    public CommentEntity create(CommentEntity commentEntity) {
        commentRepository.save(commentEntity);
        return commentEntity;
    }

    @Override
    public Set<CommentEntity> getAllByItem(Long itemId) {
        return commentRepository.findAllByItem_Id(itemId);
    }
}
