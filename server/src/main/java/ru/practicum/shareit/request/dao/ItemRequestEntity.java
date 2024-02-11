package ru.practicum.shareit.request.dao;

import lombok.Data;
import ru.practicum.shareit.user.dao.UserEntity;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Entity
@Table(schema = "PUBLIC", name = "ITEM_REQUESTS")
public class ItemRequestEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", nullable = false)
    private Long id;
    @Column(name = "DESCRIPTION", nullable = false)
    private String description;
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "REQUESTOR_ID", nullable = false)
    private UserEntity requestor;
    @Column(name = "CREATED", nullable = false)
    private LocalDateTime created;
}
