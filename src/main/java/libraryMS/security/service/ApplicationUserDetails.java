package libraryMS.security.service;

import libraryMS.domain.ApplicationUser;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Getter
@Setter
public class ApplicationUserDetails extends ApplicationUser implements UserDetails {

    private String name;
    private String password;
    private String email;
    private ApplicationUser applicationUser;
    private List<GrantedAuthority> authorities;

    public ApplicationUserDetails(ApplicationUser applicationUser) {
        name = applicationUser.getFirstName();
        password = applicationUser.getPassword();
        authorities = Arrays.stream(applicationUser.getRole().split(","))
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
        email = applicationUser.getEmail();
        this.applicationUser = applicationUser;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return !applicationUser.getIsDisabled();
    }

    public static ApplicationUserDetails getCurrentInstance() {
        if (SecurityContextHolder.getContext() == null || SecurityContextHolder.getContext().getAuthentication() == null)
            return null;
        return (ApplicationUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

    public static ApplicationUser getCurrentUser() {
        return Objects.requireNonNull(getCurrentInstance()).getApplicationUser();
    }
}
