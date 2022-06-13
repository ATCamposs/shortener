package com.url.shortener;

import com.url.shortener.controllers.params.request.LoginRequest;
import com.url.shortener.controllers.params.request.UserRegisterRequest;
import com.url.shortener.repositories.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.RequestEntity;

import java.util.Objects;

import static org.assertj.core.api.Assertions.assertThat;

@AutoConfigureTestDatabase
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ShortenerApplicationTests {
    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private UserRepository userRepository;

    @Test
    void create_account_login_and_logout() {
        // login with wrong user
        var loginParams = new LoginRequest();
        loginParams.email = "john@doe.com";
        loginParams.password = "password";

        var response = restTemplate.postForEntity(
                "/api/v1/users/login",
                loginParams,
                String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);

        // register user
        var registerParams = new UserRegisterRequest();
        registerParams.username = "john";
        registerParams.email = "john2@doe.com";
        registerParams.password = "password";

        response = restTemplate.postForEntity(
                "/api/v1/users/register",
                registerParams,
                String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);

        // login with right user
        loginParams.email = registerParams.email;
        loginParams.password = registerParams.password;

        response = restTemplate.postForEntity(
                "/api/v1/users/login",
                loginParams,
                String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(Objects.requireNonNull(response.getHeaders().get("Set-Cookie")).get(0)).contains("jwt");

        // check user profile
        var actualJwt = response.getBody().toString().split(" ")[1];

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", actualJwt);
        response = restTemplate.exchange(RequestEntity
                        .get("/api/users/me")
                        .header("Authorization", actualJwt).build(),
                String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(registerParams.username);
    }
}