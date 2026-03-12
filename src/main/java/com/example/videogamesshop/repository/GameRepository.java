package com.example.videogamesshop.repository;

import com.example.videogamesshop.entity.Game;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface GameRepository extends JpaRepository<Game, Long> {
    @Query("SELECT g FROM Game g JOIN g.categories c WHERE c.id "
            + "IN :categoryIds GROUP BY g HAVING COUNT(DISTINCT c) = :categoryCount")
    List<Game> findByCategories(@Param("categoryIds") List<Long> categoryIds,
                                @Param("categoryCount") long categoryCount);

    @Query("SELECT DISTINCT g FROM Game g "
            + "LEFT JOIN FETCH g.developer "
            + "LEFT JOIN FETCH g.publisher "
            + "LEFT JOIN FETCH g.categories")
    List<Game> findAllWithDetails();
}