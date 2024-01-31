package ru.practicum.shareit.item.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import ru.practicum.shareit.item.dao.ItemEntity;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemDtoForRequest;
import ru.practicum.shareit.item.model.Item;

@Mapper
public interface ItemMapper {
    ItemMapper ITEM_MAPPER = Mappers.getMapper(ItemMapper.class);

    @Mapping(target = "requestId", source = "itemRequest.id")
    ItemDto toDto(Item item);

    Item fromDto(ItemDto itemDto);

    ItemEntity toEntity(Item item);

    Item fromEntity(ItemEntity itemEntity);

    @Mapping(target = "requestId", source = "itemRequest.id")
    ItemDtoForRequest toRequest(ItemEntity item);
}
