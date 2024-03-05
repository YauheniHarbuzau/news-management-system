package ru.clevertec.testdatautil;

import lombok.Builder;
import lombok.Data;
import ru.clevertec.dao.entity.Role;
import ru.clevertec.dao.entity.User;

import java.util.List;

import static java.util.Collections.emptyList;

/**
 * Класс для предоставления тестовых данных
 */
@Data
@Builder(setterPrefix = "with")
public class RoleTestData {

    @Builder.Default
    private Long id = 1L;

    @Builder.Default
    private String roleType = "ADMIN";

    @Builder.Default
    private List<User> users = emptyList();

    public Role buildRole() {
        return new Role(id, roleType, users);
    }
}
