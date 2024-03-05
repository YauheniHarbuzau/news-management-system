package ru.clevertec.dao.repository;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.clevertec.dao.entity.User;

import java.util.Optional;
import java.util.UUID;

/**
 * Репозиторий для работы с {@link User}
 */
@Repository
public interface UserRepository extends JpaRepository<User, UUID> {

    @EntityGraph(attributePaths = "role")
    Optional<User> findUserByUsername(String username);

    User save(User user);
}
