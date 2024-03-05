package ru.clevertec.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.clevertec.dao.entity.Role;
import ru.clevertec.dao.entity.User;
import ru.clevertec.dao.repository.RoleRepository;
import ru.clevertec.dao.repository.UserRepository;
import ru.clevertec.exception.EntityNotFoundException;
import ru.clevertec.exception.RegistrationAuthenticationException;
import ru.clevertec.service.TokenService;
import ru.clevertec.service.UserService;
import ru.clevertec.service.dto.request.UserAuthenticationRequest;
import ru.clevertec.service.dto.request.UserRegistrationRequest;
import ru.clevertec.service.dto.response.TokenResponse;
import ru.clevertec.service.mapper.UserMapper;

/**
 * Имплементация сервиса для работы с {@link User}
 *
 * @see UserRepository
 * @see UserMapper
 * @see RoleRepository
 * @see TokenService
 * @see TokenServiceImpl
 */
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final RoleRepository roleRepository;
    private final TokenService tokenService;
    private final PasswordEncoder passwordEncoder;

    /**
     * Получение пользователя (User) по имени пользователя (username)
     *
     * @param username имя пользователя в формате String
     * @return пользователь (User)
     */
    @Override
    public User getByUsername(String username) {
        return userRepository.findUserByUsername(username)
                .orElseThrow(() -> EntityNotFoundException.of(User.class, username));
    }

    /**
     * Регистрация нового пользователя (User)
     *
     * @param userToRegistration запрос в формате UserRegistrationRequest (DTO)
     * @return ответ в формате TokenResponse (DTO)
     */
    @Override
    @Transactional
    public TokenResponse registration(UserRegistrationRequest userToRegistration) {
        var userSaved = saveUser(userToRegistration);
        var token = tokenService.generateToken(userSaved);

        return getTokenResponse(token);
    }

    /**
     * Аутентификация существующего пользователя (User)
     *
     * @param userToAuthentication запрос в формате UserAuthenticationRequest (DTO)
     * @return ответ в формате TokenResponse (DTO)
     */
    @Override
    @Transactional
    public TokenResponse authentication(UserAuthenticationRequest userToAuthentication) {
        var username = userToAuthentication.username();
        var password = userToAuthentication.password();

        var token = userRepository.findUserByUsername(username)
                .filter(user -> passwordEncoder.matches(password, user.getPassword()))
                .map(tokenService::generateToken)
                .orElseThrow(RegistrationAuthenticationException::of);

        return getTokenResponse(token);
    }

    /**
     * Вспомогательный метод для сохранения пользователя (User) в базе данных
     *
     * @param userToRegistration запрос в формате UserRegistrationRequest (DTO)
     * @return пользователь (User) после сохранения в базе данных
     */
    private User saveUser(UserRegistrationRequest userToRegistration) {
        var userToSave = userMapper.toUser(userToRegistration);
        userToSave.setPassword(passwordEncoder.encode(userToRegistration.password()));
        userToSave.setRole(getRole(userToRegistration));

        return userRepository.save(userToSave);
    }

    /**
     * Вспомогательный метод для получения Role связанной с пользователем (User)
     *
     * @param user запрос в формате UserRegistrationRequest (DTO)
     * @return Role связанная с пользователем (User)
     */
    private Role getRole(UserRegistrationRequest user) {
        var roleType = user.role();

        return roleRepository.findByRoleType(roleType)
                .orElseThrow(() -> EntityNotFoundException.of(Role.class, roleType));
    }

    /**
     * Вспомогательный метод для построения TokenResponse (DTO)
     *
     * @param token токен доступа в формате String
     * @return TokenResponse (DTO)
     */
    private TokenResponse getTokenResponse(String token) {
        return TokenResponse.builder()
                .token(token)
                .build();
    }
}
