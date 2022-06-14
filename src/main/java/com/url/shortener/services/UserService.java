package com.url.shortener.services;

import com.url.shortener.controllers.params.request.LoginRequest;
import com.url.shortener.controllers.params.request.UserRegisterRequest;
import com.url.shortener.dto.UserDto;
import com.url.shortener.models.User;
import com.url.shortener.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {
    private final PasswordService passwordService;
    private final UserRepository userRepository;
    private final JWTService<User> jwtService;

    public Page<User> findAll(Pageable pageRequest) {
        return userRepository.findAll(pageRequest);
    }

    public Optional<UserDto> findById(UUID id) {
        var allUsers = userRepository.findAll().get(0);
        var user = userRepository.findById(id);
        if (user.isPresent()) {
            return Optional.of(UserDto.MAPPER.toUserDto(user.get(), null));
        }
        return Optional.empty();
    }

    public Optional<UserDto> authenticate(LoginRequest loginRequest) {
        return userRepository.findByEmail(loginRequest.email)
                .filter(user -> passwordService.verify(loginRequest.password, user.password))
                .map(user -> {
                    var jwt = jwtService.jwtFor(user);
                    return Optional.of(UserDto.MAPPER.toUserDto(user, jwt));
                }).orElseGet(() -> Optional.empty());
    }

    public Optional<UserDto> createByRequest(UserRegisterRequest userRegisterRequest) {
        if (userRepository.existsByEmail(userRegisterRequest.email)) return Optional.empty();

        userRegisterRequest.password = passwordService.hash(userRegisterRequest.password);
        var newUser = UserRegisterRequest.MAPPER.toModel(userRegisterRequest);
        var user = userRepository.saveAndFlush(newUser);
        return Optional.of(UserDto.MAPPER.toUserDto(user, null));
    }
}
