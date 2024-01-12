package ru.practicum.shareit.user.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.Set;


public interface UserRepository extends JpaRepository<UserEntity, Long> {
    Optional<UserEntity> getUserById(Long id);
    Set<UserEntity> findAllByOrderByIdAsc();
}
