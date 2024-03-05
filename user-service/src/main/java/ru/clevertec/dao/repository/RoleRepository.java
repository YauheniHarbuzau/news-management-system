package ru.clevertec.dao.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.clevertec.dao.entity.Role;

import java.util.Optional;

/**
 * Репозиторий для работы с {@link Role}
 */
@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {

    Optional<Role> findByRoleType(String roleType);
}
