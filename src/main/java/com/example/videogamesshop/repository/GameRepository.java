package com.example.videogamesshop.repository;

import com.example.videogamesshop.entity.Game;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface GameRepository extends JpaRepository<Game, Long> {

    @Query(value = "SELECT g.* FROM games g "
            + "JOIN publishers p ON g.publisher_id = p.id "
            + "WHERE p.name = :publisherName",
            nativeQuery = true)
    List<Game> findByPublisherNameNative(@Param("publisherName") String publisherName);

    @Query("SELECT g.id FROM Game g")
    Page<Long> findGameIds(Pageable pageable);

    @Query("SELECT g.id FROM Game g JOIN g.categories c WHERE c.id "
            + "IN :categoryIds GROUP BY g HAVING COUNT(DISTINCT c) = :categoryCount")
    Page<Long> findGameIdsByCategories(@Param("categoryIds") List<Long> categoryIds,
                                       @Param("categoryCount") long categoryCount,
                                       Pageable pageable);

    @Query("SELECT DISTINCT g FROM Game g "
            + "LEFT JOIN FETCH g.developer "
            + "LEFT JOIN FETCH g.publisher "
            + "LEFT JOIN FETCH g.categories "
            + "WHERE g.id IN :ids")
    List<Game> findGamesWithDetailsByIds(@Param("ids") List<Long> ids);

    @Query("SELECT DISTINCT g FROM Game g "
            + "LEFT JOIN FETCH g.developer "
            + "LEFT JOIN FETCH g.publisher "
            + "LEFT JOIN FETCH g.categories "
            + "WHERE g.id = :id")
    Optional<Game> findByIdWithDetails(@Param("id") Long id);

    @Modifying
    @Query(value = "DELETE FROM user_game WHERE game_id = :gameId", nativeQuery = true)
    void deleteUserLinksByGameId(@Param("gameId") Long gameId);

    @Modifying
    @Query(value = "DELETE FROM game_category WHERE game_id = :gameId", nativeQuery = true)
    void deleteCategoryLinksByGameId(@Param("gameId") Long gameId);

    @Modifying
    @Query(value = "DELETE FROM user_game WHERE game_id IN (SELECT id FROM games WHERE publisher_id = :publisherId)", nativeQuery = true)
    void deleteUserLinksByPublisherId(@Param("publisherId") Long publisherId);

    @Modifying
    @Query(value = "DELETE FROM game_category WHERE game_id IN (SELECT id FROM games WHERE publisher_id = :publisherId)", nativeQuery = true)
    void deleteCategoryLinksByPublisherId(@Param("publisherId") Long publisherId);

    @Modifying
    @Query(value = "DELETE FROM games WHERE publisher_id = :publisherId", nativeQuery = true)
    void deleteGamesByPublisherId(@Param("publisherId") Long publisherId);

    default Page<Game> findAllWithDetails(Pageable pageable) {
        Page<Long> idsPage = findGameIds(pageable);
        List<Game> games = idsPage.hasContent()
                ? findGamesWithDetailsByIds(idsPage.getContent()) : Collections.emptyList();
        return new PageImpl<>(games, pageable, idsPage.getTotalElements());
    }

    default Page<Game> findByCategoriesWithDetails(List<Long> categoryIds, Pageable pageable) {
        if (categoryIds == null || categoryIds.isEmpty()) {
            return findAllWithDetails(pageable);
        }
        long categoryCount = categoryIds.size();
        Page<Long> idsPage = findGameIdsByCategories(categoryIds, categoryCount, pageable);
        List<Game> games = idsPage.hasContent()
                ? findGamesWithDetailsByIds(idsPage.getContent()) : Collections.emptyList();
        return new PageImpl<>(games, pageable, idsPage.getTotalElements());
    }
}
