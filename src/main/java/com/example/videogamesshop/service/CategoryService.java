package com.example.videogamesshop.service;

import com.example.videogamesshop.cache.GameCacheService;
import com.example.videogamesshop.dto.category.CategoryCreateRequest;
import com.example.videogamesshop.dto.category.CategoryDto;
import com.example.videogamesshop.entity.Category;
import com.example.videogamesshop.entity.Game;
import com.example.videogamesshop.exception.CategoryNotFoundException;
import com.example.videogamesshop.mapper.CategoryMapper;
import com.example.videogamesshop.repository.CategoryRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class CategoryService {
    private final CategoryRepository categoryRepository;
    private final GameCacheService cacheService;

    public List<CategoryDto> getAllCategories() {
        return categoryRepository.findAll().stream()
                .map(CategoryMapper::toCategoryDto)
                .toList();
    }

    public CategoryDto getCategoryById(Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new CategoryNotFoundException(id));
        return CategoryMapper.toCategoryDto(category);
    }

    public CategoryDto createCategory(CategoryCreateRequest request) {
        Category category = CategoryMapper.toEntity(request);
        Category savedCategory = categoryRepository.save(category);
        cacheService.clear();
        return CategoryMapper.toCategoryDto(savedCategory);
    }

    public CategoryDto updateCategory(Long id, CategoryCreateRequest request) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new CategoryNotFoundException(id));
        CategoryMapper.updateEntity(category, request);
        cacheService.clear();
        return CategoryMapper.toCategoryDto(category);
    }

    public void deleteCategory(Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new CategoryNotFoundException(id));
        for (Game game : category.getGames()) {
            game.getCategories().remove(category);
        }
        category.getGames().clear();
        categoryRepository.delete(category);
        cacheService.clear();
    }
}