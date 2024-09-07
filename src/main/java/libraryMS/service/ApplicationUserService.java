package libraryMS.service;

import libraryMS.dao.ApplicationUserDao;
import libraryMS.domain.ApplicationUser;
import libraryMS.security.service.ApplicationUserDetails;
import libraryMS.service.generic.GenericService;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;


@Service
public class ApplicationUserService extends GenericService<ApplicationUserDao, ApplicationUser, Integer> implements UserDetailsService {

    public ApplicationUserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        ApplicationUser userDetail = dao.findByEmail(email);
        if (userDetail != null) {
            return new ApplicationUserDetails(userDetail);
        } else {
            throw new UsernameNotFoundException("User not found " + email);
        }
    }
}
