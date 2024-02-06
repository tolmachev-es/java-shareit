package ru.practicum.shareit.request.service;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.HeadExeptions.InvalidParameterException;
import ru.practicum.shareit.HeadExeptions.ObjectNotFound;
import ru.practicum.shareit.item.dao.ItemEntity;
import ru.practicum.shareit.item.dao.ItemRepository;
import ru.practicum.shareit.item.dto.ItemDtoForRequest;
import ru.practicum.shareit.item.mappers.ItemMapper;
import ru.practicum.shareit.request.dao.ItemRequestEntity;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.mappers.ItemRequestMapper;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.repository.ItemRequestRepository;
import ru.practicum.shareit.user.dao.UserDao;
import ru.practicum.shareit.user.mappers.UserMapper;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@EnableJpaRepositories
public class ItemRequestServiceImpl implements ItemRequestService {
    private UserDao userDao;
    private ItemRequestRepository itemRequestRepository;
    @Resource
    private ItemRepository itemRepository;

    @Override
    public ItemRequestDto create(ItemRequestDto itemRequestDto, Long userId) {
        ItemRequest itemRequest = new ItemRequest();
        itemRequest.setDescription(itemRequestDto.getDescription());
        itemRequest.setCreated(LocalDateTime.now());
        itemRequest.setRequestor(UserMapper.USER_MAPPER.fromEntity(userDao.getUserById(userId)));
        ItemRequestEntity itemRequestEntity = ItemRequestMapper.ITEM_REQUEST_MAPPER.toEntity(itemRequest);
        itemRequestRepository.save(itemRequestEntity);
        return ItemRequestMapper.ITEM_REQUEST_MAPPER.toDto(
                ItemRequestMapper.ITEM_REQUEST_MAPPER.fromEntity(itemRequestEntity));
    }

    @Override
    public Set<ItemRequestDto> get(Long userId) {
        userDao.getUserById(userId);
        Map<Long, Set<ItemEntity>> items = getItem(itemRepository.getItemEntitiesByItemRequestNotNull());
        return itemRequestRepository.findItemRequestEntitiesByRequestor_Id(userId)
                .stream()
                .map(ItemRequestMapper.ITEM_REQUEST_MAPPER::fromEntity)
                .map(ItemRequestMapper.ITEM_REQUEST_MAPPER::toDto)
                .peek(itemRequestDto -> itemRequestDto.setItems(items.containsKey(itemRequestDto.getId())
                        ? items.get(itemRequestDto.getId())
                        .stream()
                        .map(ItemMapper.ITEM_MAPPER::toRequest)
                        .collect(Collectors.toSet()) : new HashSet<>()))
                .collect(Collectors.toSet());
    }

    @Override
    public Set<ItemRequestDto> getWithPagination(Long userId, Integer from, Integer size) {
        if (from < 0 || size < 0) {
            throw new InvalidParameterException("Неверно указаны параметры отображения");
        } else {
            Map<Long, Set<ItemEntity>> items = getItem(itemRepository.getItemEntitiesByItemRequestNotNull());
            Pageable pageable = PageRequest.of(from, size);
            return itemRequestRepository.findAllByRequestor_IdNotOrderByCreated(userId, pageable)
                    .map(ItemRequestMapper.ITEM_REQUEST_MAPPER::fromEntity)
                    .map(ItemRequestMapper.ITEM_REQUEST_MAPPER::toDto)
                    .stream()
                    .peek(itemRequestDto -> itemRequestDto.setItems(items.containsKey(itemRequestDto.getId())
                            ? items.get(itemRequestDto.getId())
                            .stream()
                            .map(ItemMapper.ITEM_MAPPER::toRequest)
                            .collect(Collectors.toSet()) : new HashSet<>()))
                    .collect(Collectors.toSet());
        }
    }

    @Override
    public ItemRequestDto getById(Long id, Long userId) {
        userDao.getUserById(userId);
        Optional<ItemRequestEntity> itemRequestEntity = itemRequestRepository.findById(id);
        if (itemRequestEntity.isPresent()) {
            ItemRequestDto itemRequest = ItemRequestMapper.ITEM_REQUEST_MAPPER.toDto(
                    ItemRequestMapper.ITEM_REQUEST_MAPPER.fromEntity(itemRequestEntity.get()));
            Set<ItemEntity> itemEntity = itemRepository.getItemEntitiesByItemRequest_Id(id);
            if (itemEntity.isEmpty()) {
                itemRequest.setItems(new HashSet<>());
            } else {
                Set<ItemDtoForRequest> itemDtoForRequests = itemEntity.stream()
                        .map(ItemMapper.ITEM_MAPPER::toRequest)
                        .collect(Collectors.toSet());
                itemRequest.setItems(itemDtoForRequests);
            }
            return itemRequest;
        } else {
            throw new ObjectNotFound(String.format("Запрос с id %s не найден", id));
        }
    }

    private Map<Long, Set<ItemEntity>> getItem(Set<ItemEntity> itemEntities) {
        Map<Long, Set<ItemEntity>> result = new HashMap<>();
        for (ItemEntity item :
                itemEntities) {
            if (result.containsKey(item.getItemRequest().getId())) {
                result.get(item.getItemRequest().getId()).add(item);
            } else {
                result.put(item.getItemRequest().getId(), Set.of(item));
            }
        }
        return result;
    }
}
