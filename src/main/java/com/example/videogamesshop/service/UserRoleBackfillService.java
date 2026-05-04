package com.example.videogamesshop.service;

import com.example.videogamesshop.entity.User;
import com.example.videogamesshop.entity.UserRole;
import com.example.videogamesshop.repository.UserRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
@Slf4j
public class UserRoleBackfillService implements ApplicationRunner {

    private final UserRepository userRepository;

    @Override
    @Transactional
    public void run(ApplicationArguments args) {
        List<User> usersWithoutRole = userRepository.findByRoleIsNull();
        if (usersWithoutRole.isEmpty()) {
            return;
        }

        usersWithoutRole.forEach(user -> user.setRole(UserRole.USER));
        log.info("Backfilled role USER for {} existing users", usersWithoutRole.size());
    }
}
