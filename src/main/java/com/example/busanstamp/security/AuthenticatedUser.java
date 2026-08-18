package com.example.busanstamp.security;

public record AuthenticatedUser(
        Long userId,
        String email,
        String role
) {
}