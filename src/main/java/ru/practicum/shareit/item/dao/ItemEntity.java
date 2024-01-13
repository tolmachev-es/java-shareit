package ru.practicum.shareit.item.dao;

import lombok.Data;
import ru.practicum.shareit.request.dao.ItemRequestEntity;
import ru.practicum.shareit.user.dao.UserEntity;

import javax.persistence.*;

@Data
@Entity
@Table(schema = "public", name = "ITEMS")
public class ItemEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private Long id;
    @Column(nullable = false)
    private String name;
    @Column(nullable = false)
    private String description;
    @Column(nullable = false)
    private Boolean available;
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "owner_id")
    private UserEntity owner;
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "item_request_id")
    private ItemRequestEntity itemRequest;
}
