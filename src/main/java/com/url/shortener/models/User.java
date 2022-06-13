package com.url.shortener.models;

import com.url.shortener.models.base.Authenticable;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.Instant;
import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "users", indexes = {
        @Index(columnList = "email", unique = true)
})
public class User implements Authenticable {
    public String username;
    public String email;
    public String password;
    public UUID refreshToken;

    @CreationTimestamp
    public Instant createdAt;

    @UpdateTimestamp
    public Instant updatedAt;

    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(updatable = false, nullable = false)
    private UUID id;

    @Override
    public UUID getId() {
        return id;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public UUID getRefreshToken() {
        return refreshToken;
    }

    @Override
    public void setRefreshToken(UUID refreshToken) {
        this.refreshToken = refreshToken;
    }
}