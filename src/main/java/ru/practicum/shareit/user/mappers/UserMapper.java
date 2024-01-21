package ru.practicum.shareit.user.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import ru.practicum.shareit.user.dao.UserEntity;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

@Mapper
public interface UserMapper {
    UserMapper USER_MAPPER = Mappers.getMapper(UserMapper.class);

    UserDto toDto(User user);

    User fromDto(UserDto user);

    UserEntity toEntity(User user);

    User fromEntity(UserEntity user);
}
