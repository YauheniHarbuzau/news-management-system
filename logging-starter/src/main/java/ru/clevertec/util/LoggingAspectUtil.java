package ru.clevertec.util;

import org.aspectj.lang.JoinPoint;
import ru.clevertec.aspect.LoggingAspect;

/**
 * Утилитарный класс, предоставляющий вспомогательные методы для {@link LoggingAspect}
 */
public class LoggingAspectUtil {

    public static String getClassName(JoinPoint joinPoint) {
        return joinPoint.getTarget().getClass().getSimpleName();
    }

    public static String getMethodName(JoinPoint joinPoint) {
        return joinPoint.getSignature().getName();
    }

    public static Object getParams(JoinPoint joinPoint) {
        return joinPoint.getArgs();
    }

    public static String getExceptionName(Throwable exception) {
        return exception.getClass().getSimpleName();
    }

    public static String getExceptionMessage(Throwable exception) {
        return exception.getMessage();
    }
}
