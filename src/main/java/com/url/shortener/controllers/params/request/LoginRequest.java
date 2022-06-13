package com.url.shortener.controllers.params.request;

import javax.validation.constraints.Email;
import javax.validation.constraints.Size;

public class LoginRequest {
    @Email(message = "Must be a valid email")
    public String email;

    @Size(min = 6, max = 20, message = "Your password must be between 6 to 20 characters")
    public String password;

}
