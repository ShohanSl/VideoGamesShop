package com.example.videogamesshop.security;

import com.example.videogamesshop.entity.User;
import com.example.videogamesshop.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AppUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;
    private final AdminCredentialsProperties adminCredentials;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        if (adminCredentials.getUsername().equals(username)) {
            return new AppUserDetails(
                    null,
                    adminCredentials.getUsername(),
                    passwordEncoder.encode(adminCredentials.getPassword()),
                    "ADMIN"
            );
        }

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Invalid credentials"));

        if (user.getPasswordHash() == null || user.getPasswordHash().isBlank()) {
            throw new UsernameNotFoundException("Invalid credentials");
        }

        return new AppUserDetails(
                user.getId(),
                user.getUsername(),
                user.getPasswordHash(),
                "USER"
        );
    }
}
