package com.example.videogamesshop.repository;

import com.example.videogamesshop.entity.User;
import com.example.videogamesshop.entity.UserRole;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
    List<User> findAllByRoleOrderByUsernameAsc(UserRole role);
    Optional<User> findByIdAndRole(Long id, UserRole role);
    List<User> findByRoleIsNull();
    long countByRole(UserRole role);
}
