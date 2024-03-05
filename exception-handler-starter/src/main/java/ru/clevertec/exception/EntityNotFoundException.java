package ru.clevertec.exception;

import ru.clevertec.constant.Constant;

import static ru.clevertec.constant.Constant.ENTITY_NOT_FOUND_EXCEPTION_MESSAGE_FORMAT;

/**
 * Исключение, генерируемое при отсутствии данных
 *
 * @see Constant
 */
public class EntityNotFoundException extends RuntimeException {

    public EntityNotFoundException(String message) {
        super(message);
    }

    public static EntityNotFoundException of(Class<?> entityClass, Object entityField) {
        return new EntityNotFoundException(
                String.format(ENTITY_NOT_FOUND_EXCEPTION_MESSAGE_FORMAT, entityClass.getSimpleName(), entityField)
        );
    }
}
