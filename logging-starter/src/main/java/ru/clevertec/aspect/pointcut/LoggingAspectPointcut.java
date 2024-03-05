package ru.clevertec.aspect.pointcut;

import org.aspectj.lang.annotation.Pointcut;
import ru.clevertec.aspect.LoggingAspect;

/**
 * Точки среза для работы АОП
 *
 * @see LoggingAspect
 */
public class LoggingAspectPointcut {

    @Pointcut("@within(ru.clevertec.annotation.Logging)")
    public static void annotationPointcut() {
    }
}
