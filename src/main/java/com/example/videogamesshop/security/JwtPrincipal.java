package com.example.videogamesshop.security;

public record JwtPrincipal(Long userId, String username, String role) {

    public boolean isAdmin() {
        return "ADMIN".equals(role);
    }
}
