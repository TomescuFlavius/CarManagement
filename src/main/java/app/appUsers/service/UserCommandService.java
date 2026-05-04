package app.appUsers.service;

import app.appUsers.dtos.UserCreateRequest;
import app.appUsers.dtos.UserCreateResponse;
import app.auth.dtos.AuthLoginRequest;
import app.auth.dtos.AuthResponse;

public interface UserCommandService {
    UserCreateResponse register(UserCreateRequest userCreateRequest);
    AuthResponse login(AuthLoginRequest authLoginRequest);
}
