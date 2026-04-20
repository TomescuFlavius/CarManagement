package app.auth.dtos;

import app.security.Permissions;

import java.util.Set;

public record AuthResponse(
        Long id,
        String name,
        String email,
        Set<Permissions> permissions,
        String token
) {}
