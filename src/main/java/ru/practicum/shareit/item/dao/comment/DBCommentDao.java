package ru.practicum.shareit.item.dao.comment;

import lombok.AllArgsConstructor;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.item.dao.comment.CommentDao;
import ru.practicum.shareit.item.dao.comment.CommentEntity;
import ru.practicum.shareit.item.dao.comment.CommentRepository;

import java.util.Set;

@Component
@AllArgsConstructor
@EnableJpaRepositories(basePackages = "ru.practicum.shareit.dao.comment")
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
