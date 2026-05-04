package app.appUsers.service;

import app.appUsers.dtos.UserCreateRequest;
import app.appUsers.dtos.UserCreateResponse;
import app.appUsers.model.AppUser;
import app.appUsers.repository.AppUserRepository;
import app.auth.dtos.AuthLoginRequest;
import app.auth.dtos.AuthResponse;
import app.cars.exceptions.handler.ApiErrorResponse;
import app.jwt.JwtTokenProvider;
import app.security.Permissions;
import jakarta.transaction.Transactional;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class UserCommandServiceImpl implements UserCommandService {
    private AppUserRepository appUserRepository;
    private PasswordEncoder passwordEncoder;
    private JwtTokenProvider jwtTokenProvider;
    private AuthenticationManager authenticationManager;

    public UserCommandServiceImpl(AuthenticationManager authenticationManager, AppUserRepository appUserRepository, PasswordEncoder passwordEncoder, JwtTokenProvider jwtTokenProvider) {
        this.authenticationManager = authenticationManager;
        this.appUserRepository = appUserRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtTokenProvider = jwtTokenProvider;

    }

    @Transactional
    @Override
    public UserCreateResponse register(UserCreateRequest userCreateRequest) {
        if (appUserRepository.findByEmail(userCreateRequest.email()).isPresent()) {
            throw new UserAlreadyExistsException();
        }
        AppUser appUser = AppUser.builder()
                .email(userCreateRequest.email())
                .name(userCreateRequest.name())
                .password(passwordEncoder.encode(userCreateRequest.password()))
                .permissionGroups(Set.of(Permissions.CAR_READ,   Permissions.USER_WRITE, Permissions.USER_READ,Permissions.WRITE_USER_PERMISSION,Permissions.READ_USER_PERMISSION))
                .build();
      AppUser appUser1=  appUserRepository.save(appUser);
        String token= jwtTokenProvider.generateToken(appUser1);
        return new UserCreateResponse(userCreateRequest.email(),token);
    }

    @Transactional
    @Override
    public AuthResponse login(AuthLoginRequest authLoginRequest) {
         try {
             authenticationManager.authenticate(
                     new UsernamePasswordAuthenticationToken(authLoginRequest.email(), authLoginRequest.password())
             );
         } catch (AuthenticationException exception) {
             throw new InvalidCredentialsException();
         }

         AppUser appUser = appUserRepository.findByEmail(authLoginRequest.email())
                 .orElseThrow(InvalidCredentialsException::new);

         return new AuthResponse(
                 appUser.getId(),
                 appUser.getName(),
                 appUser.getEmail(),
                 appUser.getPermissionGroups(),
                 jwtTokenProvider.generateToken(appUser)
         );
    }
}
