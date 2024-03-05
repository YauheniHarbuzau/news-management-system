package ru.clevertec.service.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import ru.clevertec.dao.entity.Comment;
import ru.clevertec.service.dto.request.CommentRequest;
import ru.clevertec.service.dto.response.CommentResponse;
import ru.clevertec.service.impl.CommentServiceImpl;

/**
 * Конвертер для {@link Comment}, {@link CommentRequest}, {@link CommentResponse}
 *
 * @see CommentServiceImpl
 */
@Mapper(componentModel = "spring")
public interface CommentMapper {

    CommentResponse toResponse(Comment comment);

    Comment toComment(CommentRequest commentRequest);

    Comment updateComment(@MappingTarget Comment comment, CommentRequest commentRequest);
}
