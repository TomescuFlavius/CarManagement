package app.appUsers.repository;
import app.appUsers.model.AppUser;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;


import java.util.Collection;
import java.util.List;
import java.util.Optional;
public interface AppUserRepository extends JpaRepository<AppUser, Long> {

    Optional<AppUser> findByName(@Param("name") String name);
    Optional<AppUser> findById(@Param("id") Long id);

    Optional<AppUser> findByEmail(@Email(message = "Email obligatoriu") String email);
}
