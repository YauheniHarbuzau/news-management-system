package ru.clevertec.testdatautil;

import lombok.Builder;
import lombok.Data;
import ru.clevertec.dao.entity.Role;
import ru.clevertec.dao.entity.User;
import ru.clevertec.service.dto.request.UserAuthenticationRequest;
import ru.clevertec.service.dto.request.UserRegistrationRequest;

import java.time.LocalDateTime;
import java.util.UUID;

import static java.time.Month.FEBRUARY;

/**
 * Класс для предоставления тестовых данных
 */
@Data
@Builder(setterPrefix = "with")
public class UserTestData {

    private static final LocalDateTime TEST_DATE = LocalDateTime.of(2024, FEBRUARY, 15, 9, 0, 0);

    @Builder.Default
    private Long id = 1L;

    @Builder.Default
    private UUID uuid = UUID.fromString("72d8aeda-5f2c-4080-bc36-e0222f97f078");

    @Builder.Default
    private String username = "Admin";

    @Builder.Default
    private String password = "qwerty";

    @Builder.Default
    private String email = "admin@gmail.com";

    @Builder.Default
    private Role role = RoleTestData.builder().build().buildRole();

    @Builder.Default
    private String roleType = "ADMIN";

    @Builder.Default
    private LocalDateTime createDate = TEST_DATE;

    @Builder.Default
    private LocalDateTime updateDate = TEST_DATE;

    public User buildUser() {
        return new User(id, uuid, username, password, email, role, createDate, updateDate);
    }

    public UserRegistrationRequest buildUserRegistrationRequest() {
        return new UserRegistrationRequest(username, password, email, roleType);
    }

    public UserAuthenticationRequest buildUserAuthenticationRequest() {
        return new UserAuthenticationRequest(username, password);
    }
}
