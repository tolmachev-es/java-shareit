package ru.practicum.shareit.item.dao.comment;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface CommentRepository extends JpaRepository<CommentEntity, Long> {
    Set<CommentEntity> findAllByItem_Id(Long itemId);
}
