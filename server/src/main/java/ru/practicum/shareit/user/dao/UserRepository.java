package ru.practicum.shareit.user.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.Set;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {
    Optional<UserEntity> getUserById(Long id);

    Set<UserEntity> findAllByOrderByIdAsc();
}
