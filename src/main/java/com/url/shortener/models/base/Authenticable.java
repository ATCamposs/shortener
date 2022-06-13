package com.url.shortener.models.base;

import java.util.UUID;

public interface Authenticable {
    UUID getId();

    String getPassword();

    UUID getRefreshToken();

    void setRefreshToken(UUID refreshToken);
}