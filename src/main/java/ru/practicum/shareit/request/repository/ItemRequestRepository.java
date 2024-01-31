package ru.practicum.shareit.request.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.request.dao.ItemRequestEntity;

import java.util.Set;

@Repository
public interface ItemRequestRepository extends JpaRepository<ItemRequestEntity, Long> {
    Set<ItemRequestEntity> findItemRequestEntitiesByRequestor_Id(Long userId);

    Page<ItemRequestEntity> findAllByRequestor_IdNotOrderByCreated(Long requestor_id, Pageable pageable);
}
