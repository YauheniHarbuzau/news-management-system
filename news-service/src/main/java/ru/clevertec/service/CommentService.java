package ru.clevertec.service;

import org.springframework.data.domain.Page;
import ru.clevertec.dao.entity.Comment;
import ru.clevertec.service.dto.request.CommentRequest;
import ru.clevertec.service.dto.response.CommentResponse;
import ru.clevertec.service.impl.CommentServiceImpl;

import java.util.UUID;

/**
 * Интерфейс сервиса для работы с {@link Comment}
 *
 * @see CommentServiceImpl
 */
public interface CommentService {

    CommentResponse getByUuid(UUID uuid);

    Page<CommentResponse> getAll(int pageNumber, int pageMaxSize);

    Page<CommentResponse> getAllByNewsUuid(int pageNumber, int pageMaxSize, UUID newsUuid);

    CommentResponse create(CommentRequest commentRequest);

    CommentResponse update(UUID uuid, CommentRequest commentRequest);

    void deleteByUuid(UUID uuid);
}
