package ru.practicum.shareit.item.dao;

import lombok.AllArgsConstructor;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Component
@AllArgsConstructor
@EnableJpaRepositories
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
