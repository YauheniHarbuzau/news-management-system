package ru.clevertec.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.clevertec.dao.entity.Comment;
import ru.clevertec.dao.entity.News;
import ru.clevertec.dao.repository.CommentRepository;
import ru.clevertec.dao.repository.NewsRepository;
import ru.clevertec.exception.EntityNotFoundException;
import ru.clevertec.exception.ProvideNewsByCommentException;
import ru.clevertec.service.CommentService;
import ru.clevertec.service.dto.request.CommentRequest;
import ru.clevertec.service.dto.response.CommentResponse;
import ru.clevertec.service.mapper.CommentMapper;
import ru.clevertec.util.TokenDataUtil;

import java.util.UUID;

/**
 * Имплементация сервиса для работы с {@link Comment}
 *
 * @see CommentRepository
 * @see CommentMapper
 * @see NewsRepository
 * @see TokenDataUtil
 */
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;
    private final CommentMapper commentMapper;
    private final NewsRepository newsRepository;
    private final TokenDataUtil tokenDataUtil;

    /**
     * Получение Comment по идентификатору UUID
     *
     * @param uuid идентификатор Comment в формате UUID
     * @return ответ в формате CommentResponse (DTO)
     */
    @Override
    @Cacheable(cacheNames = "comments", key = "#uuid")
    public CommentResponse getByUuid(UUID uuid) {
        var commentResponse = commentRepository.findCommentByUuid(uuid)
                .map(commentMapper::toResponse)
                .orElseThrow(() -> EntityNotFoundException.of(Comment.class, uuid));

        return CommentResponse.builder()
                .uuid(commentResponse.uuid())
                .text(commentResponse.text())
                .username(commentResponse.username())
                .createDate(commentResponse.createDate())
                .updateDate(commentResponse.updateDate())
                .newsUuid(getNewsUuidByCommentUuid(uuid))
                .build();
    }

    /**
     * Получение всех Comment с использованием пагинации
     *
     * @param pageNumber  номер страницы (для пагинации)
     * @param pageMaxSize максимальный размер страницы (для пагинации)
     * @return страница Page<CommentResponse> (список DTO)
     */
    @Override
    public Page<CommentResponse> getAll(int pageNumber, int pageMaxSize) {
        var pageable = PageRequest.of(pageNumber, pageMaxSize);

        return commentRepository.findAll(pageable)
                .map(commentMapper::toResponse);
    }

    /**
     * Получение всех Comment по идентификатору News UUID с использованием пагинации
     *
     * @param pageNumber  номер страницы (для пагинации)
     * @param pageMaxSize максимальный размер страницы (для пагинации)
     * @param newsUuid    идентификатор News в формате UUID
     * @return страница Page<CommentResponse> (список DTO)
     */
    @Override
    public Page<CommentResponse> getAllByNewsUuid(int pageNumber, int pageMaxSize, UUID newsUuid) {
        var pageable = PageRequest.of(pageNumber, pageMaxSize);

        return commentRepository.findAllByNews_Uuid(pageable, newsUuid)
                .map(commentMapper::toResponse);
    }

    /**
     * Создание нового Comment
     *
     * @param commentRequest запрос в формате CommentRequest (DTO)
     * @return ответ в формате CommentResponse (DTO)
     */
    @Override
    @Transactional
    @CachePut(cacheNames = "comments", key = "#result.uuid()")
    public CommentResponse create(CommentRequest commentRequest) {
        var commentToCreate = commentMapper.toComment(commentRequest);
        commentToCreate.setUsername(getCommentAuthor());
        commentToCreate.setNews(getNews(commentRequest));

        var commentCreated = commentRepository.save(commentToCreate);

        var commentResponse = commentMapper.toResponse(commentCreated);
        return CommentResponse.builder()
                .uuid(commentResponse.uuid())
                .text(commentResponse.text())
                .username(commentResponse.username())
                .createDate(commentResponse.createDate())
                .updateDate(commentResponse.updateDate())
                .newsUuid(commentRequest.newsUuid())
                .build();
    }

    /**
     * Обновление существующего Comment
     *
     * @param uuid           идентификатор Comment в формате UUID
     * @param commentRequest запрос в формате CommentRequest (DTO)
     * @return ответ в формате CommentResponse (DTO)
     */
    @Override
    @Transactional
    @CachePut(cacheNames = "comments", key = "#result.uuid()")
    public CommentResponse update(UUID uuid, CommentRequest commentRequest) {
        var commentInDb = commentRepository.findCommentByUuid(uuid)
                .orElseThrow(() -> EntityNotFoundException.of(Comment.class, uuid));

        var commentToUpdate = commentMapper.updateComment(commentInDb, commentRequest);
        commentToUpdate.setUsername(getCommentAuthor());
        commentToUpdate.setNews(getNews(commentRequest));

        var commentUpdated = commentRepository.save(commentToUpdate);

        var commentResponse = commentMapper.toResponse(commentUpdated);
        return CommentResponse.builder()
                .uuid(commentResponse.uuid())
                .text(commentResponse.text())
                .username(commentResponse.username())
                .createDate(commentResponse.createDate())
                .updateDate(commentResponse.updateDate())
                .newsUuid(commentRequest.newsUuid())
                .build();
    }

    /**
     * Удаление Comment по идентификатору UUID
     *
     * @param uuid идентификатор Comment в формате UUID
     */
    @Override
    @Transactional
    @CacheEvict(cacheNames = "comments", key = "#uuid")
    public void deleteByUuid(UUID uuid) {
        commentRepository.deleteCommentByUuid(uuid);
    }

    /**
     * Вспомогательный метод для получения News UUID по связанному Comment
     *
     * @param uuid идентификатор Comment в формате UUID
     * @return идентификатор News в формате UUID
     */
    private UUID getNewsUuidByCommentUuid(UUID uuid) {
        return newsRepository.findNewsUuidByCommentUuid(uuid)
                .orElseThrow(() -> ProvideNewsByCommentException.of(uuid));
    }

    /**
     * Вспомогательный метод для получения News по связанному Comment
     *
     * @param commentRequest запрос в формате CommentRequest (DTO)
     * @return News
     */
    private News getNews(CommentRequest commentRequest) {
        var newsUuid = commentRequest.newsUuid();
        return newsRepository.findNewsByUuid(newsUuid)
                .orElseThrow(() -> EntityNotFoundException.of(News.class, newsUuid));
    }

    /**
     * Вспомогательный метод для получения имени автора Comment
     *
     * @return имя автора Comment в формате String
     */
    private String getCommentAuthor() {
        return tokenDataUtil.getUsername();
    }
}
