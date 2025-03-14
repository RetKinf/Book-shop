package com.example.bookshop.mapper.impl;

import com.example.bookshop.dto.category.CategoryDto;
import com.example.bookshop.dto.category.CreateCategoryRequestDto;
import com.example.bookshop.mapper.CategoryMapper;
import com.example.bookshop.model.Category;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-03-12T12:11:32+0200",
    comments = "version: 1.6.3, compiler: javac, environment: Java 23.0.2 (Oracle Corporation)"
)
@Component
public class CategoryMapperImpl implements CategoryMapper {

    @Override
    public Category toModel(CreateCategoryRequestDto createCategoryRequestDto) {
        if ( createCategoryRequestDto == null ) {
            return null;
        }

        Category category = new Category();

        if ( createCategoryRequestDto.getName() != null ) {
            category.setName( createCategoryRequestDto.getName() );
        }
        if ( createCategoryRequestDto.getDescription() != null ) {
            category.setDescription( createCategoryRequestDto.getDescription() );
        }

        return category;
    }

    @Override
    public CategoryDto toDto(Category category) {
        if ( category == null ) {
            return null;
        }

        CategoryDto categoryDto = new CategoryDto();

        if ( category.getId() != null ) {
            categoryDto.setId( category.getId() );
        }
        if ( category.getName() != null ) {
            categoryDto.setName( category.getName() );
        }
        if ( category.getDescription() != null ) {
            categoryDto.setDescription( category.getDescription() );
        }

        return categoryDto;
    }

    @Override
    public void updateCategoryFromDto(Category category, CreateCategoryRequestDto requestDto) {
        if ( requestDto == null ) {
            return;
        }

        if ( requestDto.getName() != null ) {
            category.setName( requestDto.getName() );
        }
        else {
            category.setName( null );
        }
        if ( requestDto.getDescription() != null ) {
            category.setDescription( requestDto.getDescription() );
        }
        else {
            category.setDescription( null );
        }
    }
}
