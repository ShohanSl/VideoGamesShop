package com.example.videogamesshop.repository;

import com.example.videogamesshop.entity.Library;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LibraryRepository extends JpaRepository<Library, Long> {
}