package ru.clevertec.aop.pointcut;

import org.aspectj.lang.annotation.Pointcut;
import ru.clevertec.aop.NewsRepositoryAspect;
import ru.clevertec.dao.repository.NewsRepository;

/**
 * Точки среза для работы АОП с {@link NewsRepository}
 *
 * @see NewsRepositoryAspect
 */
public class NewsRepositoryAspectPointcut {

    @Pointcut("execution(* ru.clevertec.dao.repository.NewsRepository.findNewsByUuid(..))")
    public static void findNewsByUuidMethodPointcut() {
    }

    @Pointcut("execution(* ru.clevertec.dao.repository.NewsRepository.save(..))")
    public static void saveMethodPointcut() {
    }

    @Pointcut("execution(* ru.clevertec.dao.repository.NewsRepository.deleteNewsByUuid(..))")
    public static void deleteNewsByUuidMethodPointcut() {
    }
}
