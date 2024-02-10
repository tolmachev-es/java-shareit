package ru.practicum.shareit.request.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import ru.practicum.shareit.request.dao.ItemRequestEntity;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.model.ItemRequest;

@Mapper
public interface ItemRequestMapper {
    ItemRequestMapper ITEM_REQUEST_MAPPER = Mappers.getMapper(ItemRequestMapper.class);

    ItemRequestDto toDto(ItemRequest itemRequest);

    ItemRequest fromDto(ItemRequestDto itemRequestDto);

    ItemRequestEntity toEntity(ItemRequest itemRequest);

    ItemRequest fromEntity(ItemRequestEntity itemRequestEntity);
}
