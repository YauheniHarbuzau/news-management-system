package ru.clevertec.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import ru.clevertec.aspect.pointcut.LoggingAspectPointcut;
import ru.clevertec.constant.Constant;
import ru.clevertec.util.LoggingAspectUtil;

import static ru.clevertec.constant.Constant.ANNOTATION_POINTCUT;
import static ru.clevertec.constant.Constant.LOG_AFTER_RETURNING_FORMAT;
import static ru.clevertec.constant.Constant.LOG_AFTER_THROWING_FORMAT;
import static ru.clevertec.constant.Constant.LOG_BEFORE_FORMAT;
import static ru.clevertec.util.LoggingAspectUtil.getClassName;
import static ru.clevertec.util.LoggingAspectUtil.getExceptionMessage;
import static ru.clevertec.util.LoggingAspectUtil.getExceptionName;
import static ru.clevertec.util.LoggingAspectUtil.getMethodName;
import static ru.clevertec.util.LoggingAspectUtil.getParams;

/**
 * Реализация АОП для логирования
 *
 * @see LoggingAspectPointcut
 * @see Constant
 * @see LoggingAspectUtil
 */
@Slf4j
@Aspect
public class LoggingAspect {

    @Before(ANNOTATION_POINTCUT)
    public void logBefore(JoinPoint joinPoint) {
        log.info(LOG_BEFORE_FORMAT,
                getClassName(joinPoint),
                getMethodName(joinPoint),
                getParams(joinPoint));
    }

    @AfterReturning(value = ANNOTATION_POINTCUT, returning = "value")
    public void logAfterReturning(JoinPoint joinPoint, Object value) {
        log.info(LOG_AFTER_RETURNING_FORMAT,
                getClassName(joinPoint),
                getMethodName(joinPoint),
                getParams(joinPoint),
                value);
    }

    @AfterThrowing(value = ANNOTATION_POINTCUT, throwing = "exception")
    public void logAfterThrowing(JoinPoint joinPoint, Throwable exception) {
        log.error(LOG_AFTER_THROWING_FORMAT,
                getClassName(joinPoint),
                getMethodName(joinPoint),
                getParams(joinPoint),
                getExceptionName(exception),
                getExceptionMessage(exception));
    }
}
