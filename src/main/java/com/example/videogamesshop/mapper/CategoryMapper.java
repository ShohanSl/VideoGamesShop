package com.example.videogamesshop.mapper;

import com.example.videogamesshop.dto.category.CategoryCreateRequest;
import com.example.videogamesshop.dto.category.CategoryDto;
import com.example.videogamesshop.entity.Category;

public class CategoryMapper {

    private CategoryMapper() {
        throw new UnsupportedOperationException("This is a utility "
                + "class and cannot be instantiated");
    }

    public static CategoryDto toCategoryDto(Category category) {
        if (category == null) {
            return null;
        }
        CategoryDto dto = new CategoryDto();
        dto.setId(category.getId());
        dto.setName(category.getName());
        return dto;
    }

    public static Category toEntity(CategoryCreateRequest request) {
        if (request == null) {
            return null;
        }
        Category category = new Category();
        category.setName(request.getName());
        return category;
    }

    public static void updateEntity(Category category, CategoryCreateRequest request) {
        if (request == null) {
            return;
        }
        if (request.getName() != null) {
            category.setName(request.getName());
        }
    }
}
