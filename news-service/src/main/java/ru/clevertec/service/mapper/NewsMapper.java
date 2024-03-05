package ru.clevertec.service.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import ru.clevertec.dao.entity.News;
import ru.clevertec.service.dto.request.NewsRequest;
import ru.clevertec.service.dto.response.NewsResponse;
import ru.clevertec.service.dto.response.NewsResponseWithComment;
import ru.clevertec.service.impl.NewsServiceImpl;

/**
 * Конвертер для {@link News}, {@link NewsRequest}, {@link NewsResponse}, {@link NewsResponseWithComment}
 *
 * @see NewsServiceImpl
 */
@Mapper(componentModel = "spring")
public interface NewsMapper {

    NewsResponse toResponse(News news);

    NewsResponseWithComment toResponseWithComment(News news);

    News toNews(NewsRequest newsRequest);

    News updateNews(@MappingTarget News news, NewsRequest newsRequest);
}
