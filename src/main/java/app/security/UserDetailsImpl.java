package app.security;

import app.appUsers.repository.AppUserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@Component
public class UserDetailsImpl implements UserDetailsService {
    private AppUserRepository appUserRepository;
    public UserDetailsImpl(AppUserRepository appUserRepository) {
        this.appUserRepository = appUserRepository;
    }
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return (UserDetails) appUserRepository.findByEmail(email)
                .orElseThrow(()->new UsernameNotFoundException("User"+ email +"not found"));
    }
}
