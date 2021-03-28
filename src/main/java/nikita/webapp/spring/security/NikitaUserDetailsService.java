package nikita.webapp.spring.security;

import nikita.common.model.noark5.v5.admin.User;
import nikita.common.repository.nikita.IUserRepository;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class NikitaUserDetailsService
        implements UserDetailsService {

    private IUserRepository userRepository;

    public NikitaUserDetailsService(IUserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Transactional(readOnly = true)
    @Override
    public UserDetails loadUserByUsername(String username) throws
            UsernameNotFoundException {
        Optional<User> userOptional = userRepository.findByUsername(username);
        if (!userOptional.isPresent()) {
            throw new UsernameNotFoundException("No user found with this " +
                    "username: " + username);
        }
        User user = userOptional.get();
        Collection<? extends GrantedAuthority> authorities =
                user.getAuthorities().stream()
                        .map(authority -> new SimpleGrantedAuthority(
                                authority.getAuthorityName().name()))
                        .collect(Collectors.toList());
        return new org.springframework.security.core.userdetails.User(
                user.getUsername(), user.getPassword(), authorities);
    }
}
