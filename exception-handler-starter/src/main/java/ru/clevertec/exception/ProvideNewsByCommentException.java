package ru.clevertec.exception;

import ru.clevertec.constant.Constant;

import java.util.UUID;

import static ru.clevertec.constant.Constant.PROVIDE_NEWS_BY_COMMENT_EXCEPTION_MESSAGE_FORMAT;

/**
 * Исключение, генерируемое при отсутствии данных
 *
 * @see Constant
 */
public class ProvideNewsByCommentException extends RuntimeException {

    public ProvideNewsByCommentException(String message) {
        super(message);
    }

    public static ProvideNewsByCommentException of(UUID uuid) {
        return new ProvideNewsByCommentException(
                String.format(PROVIDE_NEWS_BY_COMMENT_EXCEPTION_MESSAGE_FORMAT, uuid)
        );
    }
}
