package ru.clevertec.dao.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.clevertec.constant.Constant;
import ru.clevertec.dao.entity.News;

import java.util.Optional;
import java.util.UUID;

import static ru.clevertec.constant.Constant.SQL_FIND_NEWS_UUID_BY_COMMENT_UUID;

/**
 * Репозиторий для работы с {@link News}
 *
 * @see Constant
 */
@Repository
public interface NewsRepository extends JpaRepository<News, UUID> {

    Optional<News> findNewsByUuid(UUID uuid);

    Page<News> findAll(Pageable pageable);

    Page<News> findAll(Specification<News> specification, Pageable pageable);

    News save(News news);

    void deleteNewsByUuid(UUID uuid);

    @Query(value = SQL_FIND_NEWS_UUID_BY_COMMENT_UUID, nativeQuery = true)
    Optional<UUID> findNewsUuidByCommentUuid(UUID commentUuid);
}
