package com.url.shortener.controllers;

import com.url.shortener.controllers.params.request.LoginRequest;
import com.url.shortener.controllers.params.request.UserRegisterRequest;
import com.url.shortener.dto.UserDto;
import com.url.shortener.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/api/v1/users", produces = MediaType.APPLICATION_JSON_VALUE)
public class UserController {
    private final UserService userService;

    @PostMapping("/login")
    ResponseEntity<UserDto> login(@RequestBody LoginRequest loginRequest) {
        return userService.authenticate(loginRequest).map(userDto -> ResponseEntity.ok().body(userDto))
                .orElseGet(() -> ResponseEntity.status(HttpStatus.UNAUTHORIZED).build());
    }

    @PostMapping(value = "/register")
    public ResponseEntity<UserDto> create(@RequestBody UserRegisterRequest request) {
        return userService.createByRequest(request).map(userDto -> ResponseEntity.status(HttpStatus.CREATED).body(userDto))
                .orElseGet(() -> ResponseEntity.status(HttpStatus.BAD_REQUEST).build());
    }

    @GetMapping(value = "/me")
    public ResponseEntity<UserDto> create(@RequestAttribute UUID userId) {
        return userService.findById(userId).map(userDto -> ResponseEntity.ok().body(userDto))
                .orElseGet(() -> ResponseEntity.status(HttpStatus.BAD_REQUEST).build());
    }
}
