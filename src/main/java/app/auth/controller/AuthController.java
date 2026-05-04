package app.auth.controller;
import app.appUsers.dtos.UserCreateRequest;
import app.appUsers.dtos.UserCreateResponse;
import app.appUsers.service.UserCommandService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
@Slf4j
@Tag(name = "Authentication", description = "Public endpoints for account registration and JWT login")
public class AuthController {
    private UserCommandService userCommandService;
    public AuthController(UserCommandService userCommandService) {
        this.userCommandService = userCommandService;
    }

    @PostMapping("/register")
    public ResponseEntity<UserCreateResponse> registerUser(@Valid @RequestBody UserCreateRequest userCreateRequest) {
        log.info("HTTP POST /api/v1/auth/register");
        return ResponseEntity.status(HttpStatus.CREATED).body(userCommandService.register(userCreateRequest));
    }

    @PostMapping("/login")
    public ResponseEntity<UserCreateResponse> loginUser(@Valid @RequestBody UserCreateRequest userCreateRequest) {
        log.info("HTTP POST /api/v1/auth/login");
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(userCommandService.login(userCreateRequest));
    }


}
