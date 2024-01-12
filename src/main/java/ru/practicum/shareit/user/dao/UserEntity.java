package ru.practicum.shareit.user.dao;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(schema = "public", name = "USERS")
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private Long id;
    @Column(nullable = false)
    private String name;
    @Column(unique = true, nullable = false)
    private String email;
}
