package ru.practicum.shareit.item.dao.comment;

import lombok.Data;
import ru.practicum.shareit.item.dao.item.ItemEntity;
import ru.practicum.shareit.user.dao.UserEntity;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Entity
@Table(schema = "PUBLIC", name = "COMMENTS")
public class CommentEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", nullable = false)
    private Long id;
    @Column(name = "TEXT", nullable = false)
    private String text;
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "ITEM_ID", nullable = false)
    private ItemEntity item;
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "AUTHOR_ID", nullable = false)
    private UserEntity author;
    @Column(name = "CREATED", nullable = false)
    private LocalDateTime created;
}
