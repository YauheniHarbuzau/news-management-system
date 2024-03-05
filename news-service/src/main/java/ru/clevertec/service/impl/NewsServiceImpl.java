package ru.clevertec.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.clevertec.dao.entity.News;
import ru.clevertec.dao.repository.NewsRepository;
import ru.clevertec.exception.EntityNotFoundException;
import ru.clevertec.service.NewsService;
import ru.clevertec.service.dto.request.NewsRequest;
import ru.clevertec.service.dto.response.NewsResponse;
import ru.clevertec.service.dto.response.NewsResponseWithComment;
import ru.clevertec.service.mapper.NewsMapper;
import ru.clevertec.util.TokenDataUtil;

import java.util.Optional;
import java.util.UUID;

/**
 * Имплементация сервиса для работы с {@link News}
 *
 * @see NewsRepository
 * @see NewsMapper
 * @see TokenDataUtil
 */
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class NewsServiceImpl implements NewsService {

    private final NewsRepository newsRepository;
    private final NewsMapper newsMapper;
    private final TokenDataUtil tokenDataUtil;

    /**
     * Получение News по идентификатору UUID
     *
     * @param uuid идентификатор News в формате UUID
     * @return ответ в формате NewsResponse (DTO)
     */
    @Override
    @Cacheable(cacheNames = "news", key = "#uuid")
    public NewsResponse getByUuid(UUID uuid) {
        return newsRepository.findNewsByUuid(uuid)
                .map(newsMapper::toResponse)
                .orElseThrow(() -> EntityNotFoundException.of(News.class, uuid));
    }

    /**
     * Получение News со списком Comment по идентификатору UUID
     *
     * @param uuid идентификатор News в формате UUID
     * @return ответ в формате NewsResponseWithComment (DTO)
     */
    @Override
    @Cacheable(cacheNames = "news", key = "#uuid")
    public NewsResponseWithComment getByUuidWithComment(UUID uuid) {
        return newsRepository.findNewsByUuid(uuid)
                .map(newsMapper::toResponseWithComment)
                .orElseThrow(() -> EntityNotFoundException.of(News.class, uuid));
    }

    /**
     * Получение всех News с использованием пагинации
     *
     * @param pageNumber  номер страницы (для пагинации)
     * @param pageMaxSize максимальный размер страницы (для пагинации)
     * @return страница Page<NewsResponse> (список DTO)
     */
    @Override
    public Page<NewsResponse> getAll(int pageNumber, int pageMaxSize) {
        var pageable = PageRequest.of(pageNumber, pageMaxSize);

        return newsRepository.findAll(pageable)
                .map(newsMapper::toResponse);
    }

    /**
     * Получение всех News с использованием поиска по задаваемому тексту и пагинации
     *
     * @param pageNumber  номер страницы (для пагинации)
     * @param pageMaxSize максимальный размер страницы (для пагинации)
     * @param text        искомый текст
     * @return страница Page<NewsResponse> (список DTO)
     */
    @Override
    public Page<NewsResponse> getAll(int pageNumber, int pageMaxSize, String text) {
        var pageable = PageRequest.of(pageNumber, pageMaxSize);

        return Optional.ofNullable(text)
                .map(txt -> "%" + txt + "%")
                .map(txt -> Specification.anyOf(
                        (Specification<News>) (root, query, criteriaBuilder) -> criteriaBuilder.like(root.get("title"), txt),
                        (Specification<News>) (root, query, criteriaBuilder) -> criteriaBuilder.like(root.get("text"), txt)
                ))
                .map(specification -> newsRepository.findAll(specification, pageable))
                .orElseGet(() -> newsRepository.findAll(pageable))
                .map(newsMapper::toResponse);
    }

    /**
     * Создание новой News
     *
     * @param newsRequest запрос в формате NewsRequest (DTO)
     * @return ответ в формате NewsResponse (DTO)
     */
    @Override
    @Transactional
    @CachePut(cacheNames = "news", key = "#result.uuid()")
    public NewsResponse create(NewsRequest newsRequest) {
        var newsToCreate = newsMapper.toNews(newsRequest);
        newsToCreate.setAuthor(getNewsAuthor());

        var newsCreated = newsRepository.save(newsToCreate);

        return newsMapper.toResponse(newsCreated);
    }

    /**
     * Обновление существующей News
     *
     * @param uuid        идентификатор News в формате UUID
     * @param newsRequest запрос в формате NewsRequest (DTO)
     * @return ответ в формате NewsResponse (DTO)
     */
    @Override
    @Transactional
    @CachePut(cacheNames = "news", key = "#result.uuid()")
    public NewsResponse update(UUID uuid, NewsRequest newsRequest) {
        var newsInDb = newsRepository.findNewsByUuid(uuid)
                .orElseThrow(() -> EntityNotFoundException.of(News.class, uuid));

        var newsToUpdate = newsMapper.updateNews(newsInDb, newsRequest);
        newsToUpdate.setAuthor(getNewsAuthor());

        var newsUpdated = newsRepository.save(newsToUpdate);
        return newsMapper.toResponse(newsUpdated);
    }

    /**
     * Удаление News по идентификатору UUID
     *
     * @param uuid идентификатор News в формате UUID
     */
    @Override
    @Transactional
    @CacheEvict(cacheNames = "news", key = "#uuid")
    public void deleteByUuid(UUID uuid) {
        newsRepository.deleteNewsByUuid(uuid);
    }

    /**
     * Вспомогательный метод для получения имени автора News
     *
     * @return имя автора News в формате String
     */
    private String getNewsAuthor() {
        return tokenDataUtil.getUsername();
    }
}
