package app.appUsers.mapper;

import app.appUsers.dtos.UserCreateRequest;
import app.appUsers.dtos.UserCreateResponse;
import app.appUsers.dtos.UserResponse;
import app.appUsers.model.AppUser;

import java.util.List;

public class UserMapper {
    public AppUser toEntity(UserCreateRequest userCreateRequest) {
        if (userCreateRequest == null) return null;
        return AppUser.builder()
                .name(userCreateRequest.name())
                .email(userCreateRequest.email())
                .password(userCreateRequest.password())
                .build();
    }

    public UserResponse toDto(AppUser appUser) {
        if (appUser == null) return null;
        return new UserResponse(
                appUser.getName(),
                appUser.getEmail(),
                appUser.getPermissionGroups()
        );
    }

    public List<UserResponse> toDtoList(List<AppUser> appUsers) {
        return appUsers.stream()
                .map(this::toDto)
                .toList();
    }
}
