package com.example.videogamesshop.security;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

@Getter
@Setter
@Validated
@Component
@ConfigurationProperties(prefix = "app.security.admin")
public class AdminCredentialsProperties {

    @NotBlank
    private String username = "admin";

    @NotBlank
    @Size(min = 12, message = "Admin password must contain at least 12 characters")
    private String password = "ChangeMeAdmin123!";
}
