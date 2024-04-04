package net.smc.services;


import net.smc.repositories.UserRepository;
import net.smc.configs.UserDetailsMapper;
import net.smc.entities.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class DatabaseUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;
    private final UserDetailsMapper userDetailsMapper;

    public DatabaseUserDetailsService(UserRepository userRepository, UserDetailsMapper userDetailsMapper) {
        this.userRepository = userRepository;
        this.userDetailsMapper = userDetailsMapper;
    }

    @Override
    public UserDetails loadUserByUsername(String login)
            throws UsernameNotFoundException {
        User userCredentials =
                userRepository.findByLogin(login);
        return userDetailsMapper.toUserDetails(userCredentials);
    }
}
