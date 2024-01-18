package ru.practicum.shareit.item.dao;

import lombok.Data;
import ru.practicum.shareit.user.dao.UserEntity;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Entity
@Table(schema = "public", name = "comments")
public class CommentEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private Long id;
    @Column(nullable = false)
    private String text;
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "item_id", nullable = false)
    private ItemEntity item;
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "author_id", nullable = false)
    private UserEntity author;
    @Column(nullable = false)
    private LocalDateTime created;
}
