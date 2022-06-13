package com.url.shortener.services;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.url.shortener.models.base.Authenticable;
import com.url.shortener.repositories.base.AuthenticableRepository;
import lombok.RequiredArgsConstructor;

import java.util.Date;
import java.util.UUID;

@RequiredArgsConstructor
public class JWTService<T extends Authenticable> {
    public static final long EXPIRATION_TIME = 900_000;
    private static final String SECRET = "796";
    public static final Algorithm ALGORITHM = Algorithm.HMAC512(SECRET.getBytes());

    private final AuthenticableRepository<T> authenticableRepository;

    public Result jwtFor(T authenticable) {
        String token = JWT.create()
                .withClaim("user_id", authenticable.getId().toString())
                .withExpiresAt(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .sign(ALGORITHM);

        authenticable.setRefreshToken(UUID.randomUUID());
        authenticableRepository.save(authenticable);

        return new Result(token, authenticable.getRefreshToken().toString());
    }

    public DecodedJWT verify(String token) {
        return JWT.require(ALGORITHM).build().verify(token);
    }

    public static class Result {
        public String token;
        public String refreshToken;

        public Result(String token, String refreshToken) {
            this.token = token;
            this.refreshToken = refreshToken;
        }
    }
}