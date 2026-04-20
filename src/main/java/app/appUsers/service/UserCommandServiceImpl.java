package app.appUsers.service;

import app.appUsers.dtos.UserCreateRequest;
import app.appUsers.dtos.UserCreateResponse;
import app.appUsers.model.AppUser;
import app.appUsers.repository.AppUserRepository;
import app.jwt.JwtTokenProvider;
import app.security.Permissions;
import jakarta.transaction.Transactional;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Locale;
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
        if (appUserRepository.findByEmail(userCreateRequest.email()).isPresent()) throw new IllegalArgumentException("User already exists");
        AppUser appUser = AppUser.builder()
                .email(userCreateRequest.email())
                .name(userCreateRequest.name())
                .password(passwordEncoder.encode(userCreateRequest.password()))
                .permissionGroups(Set.of(Permissions.CAR_READ))
                .build();
        appUserRepository.save(appUser);
        String token= jwtTokenProvider.generateToken(appUser);
        return new UserCreateResponse(userCreateRequest.email(),token);
    }

    @Transactional
    @Override
    public UserCreateResponse login(UserCreateRequest userCreateRequest) {
         authenticationManager.authenticate( new UsernamePasswordAuthenticationToken(userCreateRequest.email(), userCreateRequest.password()));
         if (appUserRepository.findByEmail(userCreateRequest.email()).isEmpty()) throw new IllegalArgumentException("Email incorrect");
         AppUser appUser = appUserRepository.findByEmail(userCreateRequest.email()).get();
         appUser.setEmail(userCreateRequest.email());
         appUser.setName(appUser.getName());
         appUser.setPermissionGroups(Set.of(Permissions.CAR_READ));
         appUser.setPassword(passwordEncoder.encode(userCreateRequest.password()));
         return new UserCreateResponse(userCreateRequest.email(),jwtTokenProvider.generateToken(appUser));
    }


}
