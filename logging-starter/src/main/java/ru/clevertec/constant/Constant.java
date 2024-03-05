package ru.clevertec.constant;

/**
 * Класс для хранения констант
 */
public class Constant {

    public static final String ANNOTATION_POINTCUT = "ru.clevertec.aspect.pointcut.LoggingAspectPointcut.annotationPointcut()";

    public static final String LOG_BEFORE_FORMAT = "class {}; method {}(); params {}";
    public static final String LOG_AFTER_RETURNING_FORMAT = "class {}; method {}(); params {}; return {}";
    public static final String LOG_AFTER_THROWING_FORMAT = "class {}; method {}(); params {}; throw {} - {}";
}
