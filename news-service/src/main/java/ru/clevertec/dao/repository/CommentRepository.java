package ru.clevertec.dao.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.clevertec.dao.entity.Comment;

import java.util.Optional;
import java.util.UUID;

/**
 * Репозиторий для работы с {@link Comment}
 */
@Repository
public interface CommentRepository extends JpaRepository<Comment, UUID> {

    Optional<Comment> findCommentByUuid(UUID uuid);

    Page<Comment> findAll(Pageable pageable);

    Page<Comment> findAllByNews_Uuid(Pageable pageable, UUID newsUuid);

    Comment save(Comment comment);

    void deleteCommentByUuid(UUID uuid);
}
