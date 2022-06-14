package com.url.shortener;

import com.url.shortener.controllers.params.request.LoginRequest;
import com.url.shortener.controllers.params.request.UserRegisterRequest;
import com.url.shortener.dto.UserDto;
import com.url.shortener.repositories.UserRepository;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.RequestEntity;

import static org.assertj.core.api.Assertions.assertThat;

@AutoConfigureTestDatabase
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ShortenerApplicationTests {
    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private UserRepository userRepository;

    @Test
    void create_account_login_and_logout() throws JSONException {
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
        var jsonResponse = new JSONObject(response.getBody());

        var actualJwt = jsonResponse.getJSONObject("jwt").get("token").toString();

        assertThat(actualJwt).isNotNull();

        // check user profile
        var userProfileResponse = restTemplate.exchange(RequestEntity
                        .get("/api/v1/users/me")
                        .header("Authorization", "Bearer " + actualJwt).build(),
                UserDto.class);
        assertThat(userProfileResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        var user = userRepository.findAll().get(0);
        var responseBody = userProfileResponse.getBody();
        assertThat(responseBody.username).isEqualTo(user.getUsername());
        assertThat(responseBody.email).isEqualTo(user.getEmail());
        assertThat(responseBody.createdAt).isEqualTo(user.getCreatedAt());
        assertThat(responseBody.updatedAt).isEqualTo(user.getUpdatedAt());
    }
}