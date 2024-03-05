package ru.clevertec.aop;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import ru.clevertec.aop.pointcut.CommentRepositoryAspectPointcut;
import ru.clevertec.cache.Cache;
import ru.clevertec.cache.CacheFactory;
import ru.clevertec.cache.impl.CacheFactoryImpl;
import ru.clevertec.dao.entity.Comment;
import ru.clevertec.dao.repository.CommentRepository;

import java.util.Optional;
import java.util.UUID;

/**
 * Реализация АОП для кэширования данных (на уровне репозитория)
 *
 * @see CommentRepository
 * @see CommentRepositoryAspectPointcut
 * @see Cache
 * @see CacheFactory
 */
@Aspect
@Component
@Profile("dev")
public class CommentRepositoryAspect {

    private final CacheFactory<UUID, Comment> cacheFactory;
    private final Cache<UUID, Comment> cache;

    public CommentRepositoryAspect(@Value("${cache.type}") String cacheType,
                                   @Value("${cache.capacity}") int cacheCapacity) {
        this.cacheFactory = new CacheFactoryImpl<>();
        this.cache = cacheFactory.initCache(cacheType, cacheCapacity);
    }

    @Around("ru.clevertec.aop.pointcut.CommentRepositoryAspectPointcut.findCommentByUuidMethodPointcut()")
    public Optional<Comment> aroundFindCommentByUuidMethod(ProceedingJoinPoint joinPoint) throws Throwable {
        var uuid = (UUID) joinPoint.getArgs()[0];

        Optional<Comment> comment;
        if (cache.get(uuid).isPresent()) {
            comment = cache.get(uuid);
        } else {
            comment = (Optional<Comment>) joinPoint.proceed();
            comment.ifPresent(p -> cache.put(p.getUuid(), p));
        }

        return comment;
    }

    @Around("ru.clevertec.aop.pointcut.CommentRepositoryAspectPointcut.saveMethodPointcut()")
    public Comment aroundSaveMethod(ProceedingJoinPoint joinPoint) throws Throwable {
        var comment = (Comment) joinPoint.proceed();
        cache.put(comment.getUuid(), comment);
        return comment;
    }

    @After("ru.clevertec.aop.pointcut.CommentRepositoryAspectPointcut.deleteCommentByUuidMethodPointcut()")
    public void afterDeleteCommentByUuidMethod(JoinPoint joinPoint) {
        var uuid = (UUID) joinPoint.getArgs()[0];

        if (cache.get(uuid).isPresent()) {
            cache.remove(uuid);
        }
    }
}
