package ru.practicum.shareit.request.service;

import ru.practicum.shareit.request.dto.ItemRequestDto;

import java.util.Set;

public interface ItemRequestService {
    ItemRequestDto create(ItemRequestDto itemRequestDto, Long userId);

    Set<ItemRequestDto> get(Long userId);

    Set<ItemRequestDto> getWithPagination(Long userId, Integer from, Integer size);

    ItemRequestDto getById(Long id, Long userId);
}
