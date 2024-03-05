package ru.clevertec.service.impl;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import ru.clevertec.dao.entity.Role;
import ru.clevertec.dao.entity.User;
import ru.clevertec.dao.repository.RoleRepository;
import ru.clevertec.dao.repository.UserRepository;
import ru.clevertec.exception.EntityNotFoundException;
import ru.clevertec.exception.RegistrationAuthenticationException;
import ru.clevertec.service.TokenService;
import ru.clevertec.service.dto.request.UserAuthenticationRequest;
import ru.clevertec.service.dto.request.UserRegistrationRequest;
import ru.clevertec.service.mapper.UserMapper;
import ru.clevertec.service.mapper.UserMapperImpl;
import ru.clevertec.testdatautil.RoleTestData;
import ru.clevertec.testdatautil.UserTestData;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

/**
 * Тестовый класс для {@link UserServiceImpl}
 *
 * @see UserTestData
 * @see RoleTestData
 */
@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @InjectMocks
    private UserServiceImpl userService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private TokenService tokenService;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Spy
    private final UserMapper userMapper = new UserMapperImpl();

    @Captor
    private ArgumentCaptor<User> userCaptor;

    @Nested
    class GetByUsernameTest {
        @Test
        void checkGetByUsernameShouldReturnCorrectUser() {
            // given
            String username = "Admin";

            User expectedUser = UserTestData.builder()
                    .build().buildUser();

            doReturn(Optional.of(expectedUser))
                    .when(userRepository).findUserByUsername(username);

            // when
            User actualUser = userService.getByUsername(username);

            // then
            assertEquals(expectedUser, actualUser);
        }

        @Test
        void checkGetByUsernameShouldThrowEntityNotFoundException() {
            // given
            String usernameFound = "Admin";
            String usernameNotFound = "Unknown User";

            User user = UserTestData.builder()
                    .withUsername(usernameFound)
                    .build().buildUser();

            doReturn(Optional.of(user))
                    .when(userRepository).findUserByUsername(usernameFound);

            // when, then
            assertAll(
                    () -> assertDoesNotThrow(() -> userService.getByUsername(usernameFound)),
                    () -> assertThrows(EntityNotFoundException.class, () -> userService.getByUsername(usernameNotFound))
            );
        }
    }

    @Nested
    class RegistrationTest {
        @Test
        void checkRegistrationShouldSavedCorrectUser() {
            // given
            UserRegistrationRequest userRequest = UserTestData.builder()
                    .build().buildUserRegistrationRequest();
            Role role = RoleTestData.builder()
                    .build().buildRole();

            doReturn(Optional.of(role))
                    .when(roleRepository).findByRoleType(userRequest.role());

            // when
            userService.registration(userRequest);

            // then
            verify(userRepository, times(1)).save(userCaptor.capture());
            User actualUser = userCaptor.getValue();
            assertAll(
                    () -> assertThat(actualUser.getUsername()).isEqualTo(userRequest.username()),
                    () -> assertThat(actualUser.getEmail()).isEqualTo(userRequest.email()),
                    () -> assertThat(actualUser.getRole().getRoleType()).isEqualTo(userRequest.role())
            );
        }
    }

    @Nested
    class AuthenticationTest {
        @Test
        void checkAuthenticationShouldReturnCorrectToken() {
            // given
            UserAuthenticationRequest userRequest = UserTestData.builder()
                    .build().buildUserAuthenticationRequest();
            User userInDb = UserTestData.builder()
                    .build().buildUser();
            String expectedToken = "JWT Token";

            doReturn(Optional.of(userInDb))
                    .when(userRepository).findUserByUsername(userRequest.username());
            doReturn(true)
                    .when(passwordEncoder).matches(userRequest.password(), userInDb.getPassword());
            doReturn(expectedToken)
                    .when(tokenService).generateToken(userInDb);

            // when
            String actualToken = userService.authentication(userRequest).token();

            // then
            assertEquals(expectedToken, actualToken);
        }

        @Test
        void checkAuthenticationShouldThrowRegistrationAuthenticationException() {
            // given
            UserAuthenticationRequest userRequest = UserTestData.builder()
                    .build().buildUserAuthenticationRequest();

            doThrow(RegistrationAuthenticationException.class)
                    .when(userRepository).findUserByUsername(userRequest.username());

            // when, then
            assertThrows(RegistrationAuthenticationException.class, () -> userService.authentication(userRequest));
        }
    }
}
