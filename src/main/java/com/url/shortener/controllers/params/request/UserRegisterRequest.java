package com.url.shortener.controllers.params.request;

import com.url.shortener.models.User;
import org.mapstruct.Mapper;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

import static org.mapstruct.factory.Mappers.getMapper;

public class UserRegisterRequest {
    public final static Mappers MAPPER = getMapper(Mappers.class);
    @NotEmpty
    public String username;

    @Email
    public String email;

    @Size(min = 6, max = 20, message = "Your password must be between 6 to 20 characters")
    public String password;

    @Mapper
    public interface Mappers {
        User toModel(UserRegisterRequest params);
    }
}
