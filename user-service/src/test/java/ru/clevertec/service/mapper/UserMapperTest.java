package ru.clevertec.service.mapper;

import org.junit.jupiter.api.Test;
import ru.clevertec.dao.entity.User;
import ru.clevertec.service.dto.request.UserRegistrationRequest;
import ru.clevertec.testdatautil.UserTestData;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.isNull;

/**
 * Тестовый класс для {@link UserMapper}
 *
 * @see UserTestData
 */
class UserMapperTest {

    private final UserMapper userMapper = new UserMapperImpl();

    @Test
    void checkToUserShouldReturnCorrectUser() {
        // given
        UserRegistrationRequest mappingUserRegistrationRequest = UserTestData.builder()
                .build().buildUserRegistrationRequest();

        // when
        User actualUser = userMapper.toUser(mappingUserRegistrationRequest);

        // than
        assertThat(actualUser)
                .hasFieldOrPropertyWithValue(User.Fields.id, isNull())
                .hasFieldOrPropertyWithValue(User.Fields.uuid, isNull())
                .hasFieldOrPropertyWithValue(User.Fields.username, mappingUserRegistrationRequest.username())
                .hasFieldOrPropertyWithValue(User.Fields.password, mappingUserRegistrationRequest.password())
                .hasFieldOrPropertyWithValue(User.Fields.email, mappingUserRegistrationRequest.email())
                .hasFieldOrPropertyWithValue(User.Fields.role, isNull())
                .hasFieldOrPropertyWithValue(User.Fields.createDate, isNull())
                .hasFieldOrPropertyWithValue(User.Fields.updateDate, isNull());
    }
}
