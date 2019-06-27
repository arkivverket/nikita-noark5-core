package nikita.webapp.spring.security;

import nikita.common.model.nikita.NikitaUserPrincipal;
import nikita.common.model.noark5.v5.admin.User;
import nikita.common.repository.nikita.IUserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class NikitaUserDetailsService
        implements UserDetailsService {

    private IUserRepository userRepository;

    public NikitaUserDetailsService(IUserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws
            UsernameNotFoundException {

        Optional<User> user = userRepository.findByUsername(username);
        if (!user.isPresent()) {
            throw new UsernameNotFoundException("No user found with this " +
                    "username: " + username);
        }
        return new NikitaUserPrincipal(user.get());
    }
}
