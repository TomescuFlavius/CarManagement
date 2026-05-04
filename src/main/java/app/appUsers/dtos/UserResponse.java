package app.appUsers.dtos;

import app.security.Permissions;

import java.util.Set;

public record UserResponse (
        String email,
        String name,
        Set<Permissions> permissions

) {}
