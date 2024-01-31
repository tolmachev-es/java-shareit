package ru.practicum.shareit.item.dao;

import lombok.Data;
import ru.practicum.shareit.request.dao.ItemRequestEntity;
import ru.practicum.shareit.user.dao.UserEntity;

import javax.persistence.*;

@Data
@Entity
@Table(schema = "PUBLIC", name = "ITEMS")
public class ItemEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", nullable = false)
    private Long id;
    @Column(name = "NAME", nullable = false)
    private String name;
    @Column(name = "DESCRIPRION", nullable = false)
    private String description;
    @Column(name = "AVAILABLE", nullable = false)
    private Boolean available;
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "OWNER_ID", nullable = false)
    private UserEntity owner;
    @OneToOne
    @JoinColumn(name = "ITEM_REQUEST_ID")
    private ItemRequestEntity itemRequest;
}
