package libraryMS;

import libraryMS.dao.ApplicationUserDao;
import libraryMS.domain.ApplicationUser;
import libraryMS.utils.constants.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ApplicationRunSuccessfullyEvent {
    private final PasswordEncoder passwordEncoder;

    private final ApplicationUserDao applicationUserDao;

    @EventListener(ApplicationReadyEvent.class)
    public void handleSuccessful() {
        System.out.println("Application run successfully......");
        ApplicationUser currentAdmin = applicationUserDao.findByEmail("admin@gmail.com");
        ApplicationUser currentUser = applicationUserDao.findByEmail("user@gmail.com");
        if (currentAdmin == null) {
            ApplicationUser admin = ApplicationUser.builder()
                    .email("admin@gmail.com")
                    .firstName("elie")
                    .lastName("karra")
                    .role(Role.ROLE_ADMIN.name())
                    .isDisabled(false)
                    .password(passwordEncoder.encode("12345678")).build();

            applicationUserDao.save(admin);
        }
        if (currentUser == null) {
            ApplicationUser user = ApplicationUser.builder()
                    .email("user@gmail.com")
                    .firstName("elie2")
                    .lastName("karra2")
                    .role(Role.ROLE_USER.name())
                    .isDisabled(false)
                    .password(passwordEncoder.encode("12345678")).build();


            applicationUserDao.save(user);
        }
    }
}
