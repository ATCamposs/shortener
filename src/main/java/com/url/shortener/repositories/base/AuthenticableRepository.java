package com.url.shortener.repositories.base;

import com.url.shortener.models.base.Authenticable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

import java.util.UUID;

@NoRepositoryBean
public interface AuthenticableRepository<T extends Authenticable> extends JpaRepository<T, UUID> {
}