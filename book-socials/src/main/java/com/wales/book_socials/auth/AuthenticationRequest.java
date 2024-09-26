package com.wales.book_socials.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Builder
public class AuthenticationRequest {
    @NotEmpty(message = "Email should not be empty")
    @NotBlank(message = "Email should not be empty")
    @Email(message = "Email should be valid")
    private String email;

    @NotEmpty(message = "Password should not be empty")
    @NotBlank(message = "Password should not be empty")
    @Size(min = 8, message = "Password should be at least 8 characters")
    private String password;
}
