package ru.practicum.shareit.item.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;

@Mapper
public interface ItemMapper {
    ItemMapper ITEM_MAPPER = Mappers.getMapper(ItemMapper.class);
    ItemDto toDto(Item item);
}
