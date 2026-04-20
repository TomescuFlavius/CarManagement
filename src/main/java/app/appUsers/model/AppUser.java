package app.appUsers.model;

import app.security.Permissions;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString(onlyExplicitlyIncluded = true)
@Entity
@Table(name = "app_users")
public class AppUser implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @ToString.Include
    private Long id;

    @NotBlank(message = "Prenume obligatoriu")
    @Size(min = 3, max = 50, message = "Size intre 3-50")
    @Column(nullable = false)
    @ToString.Include
    private String name;

    @Email
    @NotBlank(message = "Email obligatoriu")
    @Column(nullable = false, unique = true)
    @ToString.Include
    private String email;

    @NotBlank
    @Column(nullable = false)
    private String password;

    @Builder.Default
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(
            name = "permission_groups",
            joinColumns = @JoinColumn(name = "user_id")
    )
    @Column(name = "permission_group", nullable = false)
    @Enumerated(EnumType.STRING)
    private Set<Permissions> permissionGroups = new HashSet<>();

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return permissionGroups.stream()
                .map(Permissions::getPermission)
                .map(SimpleGrantedAuthority::new)
                .toList();
    }

    @Override
    public String getUsername() {
        return this.email;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AppUser other)) return false;
        return id != null && id.equals(other.id);
    }

    @Override
    public int hashCode() {
        // constant hash => stable între transient și managed, fără a strica colecțiile
        return Objects.hash(getClass());
    }
}