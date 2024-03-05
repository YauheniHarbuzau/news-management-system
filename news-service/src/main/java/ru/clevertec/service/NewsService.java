package ru.clevertec.service;

import org.springframework.data.domain.Page;
import ru.clevertec.dao.entity.News;
import ru.clevertec.service.dto.request.NewsRequest;
import ru.clevertec.service.dto.response.NewsResponse;
import ru.clevertec.service.dto.response.NewsResponseWithComment;
import ru.clevertec.service.impl.NewsServiceImpl;

import java.util.UUID;

/**
 * Интерфейс сервиса для работы с {@link News}
 *
 * @see NewsServiceImpl
 */
public interface NewsService {

    NewsResponse getByUuid(UUID uuid);

    NewsResponseWithComment getByUuidWithComment(UUID uuid);

    Page<NewsResponse> getAll(int pageNumber, int pageMaxSize);

    Page<NewsResponse> getAll(int pageNumber, int pageMaxSize, String text);

    NewsResponse create(NewsRequest newsRequest);

    NewsResponse update(UUID uuid, NewsRequest newsRequest);

    void deleteByUuid(UUID uuid);
}
