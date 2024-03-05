package ru.clevertec.aop;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import ru.clevertec.aop.pointcut.NewsRepositoryAspectPointcut;
import ru.clevertec.cache.Cache;
import ru.clevertec.cache.CacheFactory;
import ru.clevertec.cache.impl.CacheFactoryImpl;
import ru.clevertec.dao.entity.News;
import ru.clevertec.dao.repository.NewsRepository;

import java.util.Optional;
import java.util.UUID;

/**
 * Реализация АОП для кэширования данных (на уровне репозитория)
 *
 * @see NewsRepository
 * @see NewsRepositoryAspectPointcut
 * @see Cache
 * @see CacheFactory
 */
@Aspect
@Component
@Profile("dev")
public class NewsRepositoryAspect {

    private final CacheFactory<UUID, News> cacheFactory;
    private final Cache<UUID, News> cache;

    public NewsRepositoryAspect(@Value("${cache.type}") String cacheType,
                                @Value("${cache.capacity}") int cacheCapacity) {
        this.cacheFactory = new CacheFactoryImpl<>();
        this.cache = cacheFactory.initCache(cacheType, cacheCapacity);
    }

    @Around("ru.clevertec.aop.pointcut.NewsRepositoryAspectPointcut.findNewsByUuidMethodPointcut()")
    public Optional<News> aroundFindNewsByUuidMethod(ProceedingJoinPoint joinPoint) throws Throwable {
        var uuid = (UUID) joinPoint.getArgs()[0];

        Optional<News> news;
        if (cache.get(uuid).isPresent()) {
            news = cache.get(uuid);
        } else {
            news = (Optional<News>) joinPoint.proceed();
            news.ifPresent(p -> cache.put(p.getUuid(), p));
        }

        return news;
    }

    @Around("ru.clevertec.aop.pointcut.NewsRepositoryAspectPointcut.saveMethodPointcut()")
    public News aroundSaveMethod(ProceedingJoinPoint joinPoint) throws Throwable {
        var news = (News) joinPoint.proceed();
        cache.put(news.getUuid(), news);
        return news;
    }

    @After("ru.clevertec.aop.pointcut.NewsRepositoryAspectPointcut.deleteNewsByUuidMethodPointcut()")
    public void afterDeleteNewsByUuidMethod(JoinPoint joinPoint) {
        var uuid = (UUID) joinPoint.getArgs()[0];

        if (cache.get(uuid).isPresent()) {
            cache.remove(uuid);
        }
    }
}
