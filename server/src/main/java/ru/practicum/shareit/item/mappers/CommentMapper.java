package ru.practicum.shareit.item.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import ru.practicum.shareit.item.dao.comment.CommentEntity;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.model.Comment;

@Mapper
public interface CommentMapper {
    CommentMapper COMMENT_MAPPER = Mappers.getMapper(CommentMapper.class);

    @Mapping(target = "authorName", source = "author.name")
    CommentDto toDto(Comment comment);

    Comment fromDto(CommentDto commentDto);

    CommentEntity toEntity(Comment comment);

    Comment fromEntity(CommentEntity commentEntity);
}
