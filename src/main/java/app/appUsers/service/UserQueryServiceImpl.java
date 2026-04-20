package app.appUsers.service;

import app.appUsers.model.AppUser;
import app.appUsers.repository.AppUserRepository;
import org.springframework.stereotype.Service;

@Service
public class UserQueryServiceImpl implements UserQueryService {
    private AppUserRepository appUserRepository;
    public UserQueryServiceImpl(AppUserRepository appUserRepository) {
        this.appUserRepository = appUserRepository;
    }

}
