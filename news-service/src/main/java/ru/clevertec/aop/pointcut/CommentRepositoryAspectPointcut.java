package ru.clevertec.aop.pointcut;

import org.aspectj.lang.annotation.Pointcut;
import ru.clevertec.aop.CommentRepositoryAspect;
import ru.clevertec.dao.repository.CommentRepository;

/**
 * Точки среза для работы АОП с {@link CommentRepository}
 *
 * @see CommentRepositoryAspect
 */
public class CommentRepositoryAspectPointcut {

    @Pointcut("execution(* ru.clevertec.dao.repository.CommentRepository.findCommentByUuid(..))")
    public static void findCommentByUuidMethodPointcut() {
    }

    @Pointcut("execution(* ru.clevertec.dao.repository.CommentRepository.save(..))")
    public static void saveMethodPointcut() {
    }

    @Pointcut("execution(* ru.clevertec.dao.repository.CommentRepository.deleteCommentByUuid(..))")
    public static void deleteCommentByUuidMethodPointcut() {
    }
}
