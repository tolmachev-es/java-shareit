package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;

import java.util.Set;

public interface ItemService {
    ItemDto create(ItemDto item, long userId);

    ItemDto update(ItemDto item, long userId, long itemId);

    ItemDto get(Long itemId, Long userId);

    Set<ItemDto> getAllByOwner(Long userId, Integer from, Integer size);

    Set<ItemDto> search(String text, Integer from, Integer size);

    CommentDto addComment(Long userId, Long itemId, CommentDto commentDto);
}
