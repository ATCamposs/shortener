package com.url.shortener.repositories;

import com.url.shortener.models.User;
import com.url.shortener.repositories.base.AuthenticableRepository;

import java.util.Optional;

public interface UserRepository extends AuthenticableRepository<User> {
    Optional<User> findByEmail(String email);

    Boolean existsByEmail(String email);
}
