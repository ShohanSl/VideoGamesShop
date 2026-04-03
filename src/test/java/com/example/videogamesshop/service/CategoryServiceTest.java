package com.example.videogamesshop.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.example.videogamesshop.cache.GameCacheService;
import com.example.videogamesshop.dto.category.CategoryCreateRequest;
import com.example.videogamesshop.dto.category.CategoryDto;
import com.example.videogamesshop.entity.Category;
import com.example.videogamesshop.entity.Game;
import com.example.videogamesshop.exception.CategoryNotFoundException;
import com.example.videogamesshop.repository.CategoryRepository;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class CategoryServiceTest {

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private GameCacheService cacheService;

    @InjectMocks
    private CategoryService categoryService;

    @Test
    void shouldReturnAllCategories() {
        Category category = new Category();
        category.setId(1L);
        category.setName("RPG");
        when(categoryRepository.findAll()).thenReturn(List.of(category));

        List<CategoryDto> result = categoryService.getAllCategories();

        assertEquals(1, result.size());
        assertEquals("RPG", result.get(0).getName());
    }

    @Test
    void shouldReturnCategoryById() {
        Category category = new Category();
        category.setId(2L);
        category.setName("Action");
        when(categoryRepository.findById(2L)).thenReturn(Optional.of(category));

        CategoryDto result = categoryService.getCategoryById(2L);

        assertEquals(2L, result.getId());
        assertEquals("Action", result.getName());
    }

    @Test
    void shouldCreateCategoryAndClearCache() {
        CategoryCreateRequest request = new CategoryCreateRequest();
        request.setName("Adventure");
        Category saved = new Category();
        saved.setId(10L);
        saved.setName("Adventure");
        when(categoryRepository.save(org.mockito.ArgumentMatchers.any(Category.class)))
                .thenReturn(saved);

        CategoryDto result = categoryService.createCategory(request);

        assertEquals(10L, result.getId());
        assertEquals("Adventure", result.getName());
        verify(cacheService).clear();
    }

    @Test
    void shouldUpdateCategoryAndClearCache() {
        Category category = new Category();
        category.setId(3L);
        category.setName("Old");
        CategoryCreateRequest request = new CategoryCreateRequest();
        request.setName("New");
        when(categoryRepository.findById(3L)).thenReturn(Optional.of(category));

        CategoryDto result = categoryService.updateCategory(3L, request);

        assertEquals("New", result.getName());
        verify(cacheService).clear();
    }

    @Test
    void shouldDeleteCategoryAndRemoveItFromGames() {
        Category category = new Category();
        category.setId(5L);
        category.setName("Strategy");
        Game game = new Game();
        game.setId(7L);
        game.setCategories(new HashSet<>(Set.of(category)));
        category.setGames(new HashSet<>(Set.of(game)));
        when(categoryRepository.findById(5L)).thenReturn(Optional.of(category));

        categoryService.deleteCategory(5L);

        assertEquals(0, game.getCategories().size());
        assertEquals(0, category.getGames().size());
        verify(categoryRepository).delete(category);
        verify(cacheService).clear();
    }

    @Test
    void shouldThrowWhenCategoryNotFound() {
        when(categoryRepository.findById(100L)).thenReturn(Optional.empty());

        assertThrows(CategoryNotFoundException.class,
                () -> categoryService.getCategoryById(100L));
    }

    @Test
    void shouldThrowWhenUpdatingMissingCategory() {
        CategoryCreateRequest request = new CategoryCreateRequest();
        request.setName("Missing");
        when(categoryRepository.findById(101L)).thenReturn(Optional.empty());

        assertThrows(CategoryNotFoundException.class,
                () -> categoryService.updateCategory(101L, request));
    }

    @Test
    void shouldThrowWhenDeletingMissingCategory() {
        when(categoryRepository.findById(102L)).thenReturn(Optional.empty());

        assertThrows(CategoryNotFoundException.class,
                () -> categoryService.deleteCategory(102L));
    }
}
