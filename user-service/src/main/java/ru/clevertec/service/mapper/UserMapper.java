package ru.clevertec.service.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.clevertec.dao.entity.User;
import ru.clevertec.service.dto.request.UserRegistrationRequest;
import ru.clevertec.service.impl.UserServiceImpl;

/**
 * Конвертер для {@link User}, {@link UserRegistrationRequest}
 *
 * @see UserServiceImpl
 */
@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(target = "role", ignore = true)
    User toUser(UserRegistrationRequest userRegistrationRequest);
}
