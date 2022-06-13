package com.url.shortener.dto;

import com.url.shortener.models.User;
import com.url.shortener.services.JWTService;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.time.Instant;

import static org.mapstruct.factory.Mappers.getMapper;

public class UserDto {
    public final static Mappers MAPPER = getMapper(Mappers.class);
    public String username;
    public String email;
    public Instant createdAt;
    public Instant updatedAt;
    public JWTService.Result jwt;

    @Mapper
    public interface Mappers {
        @Mapping(target = "jwt", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
        UserDto toUserDto(User user, JWTService.Result jwt);
    }
}
