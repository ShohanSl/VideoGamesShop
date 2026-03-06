package com.example.videogamesshop.repository;

import com.example.videogamesshop.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}