package com.example.bookshop.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import com.example.bookshop.dto.category.CategoryDto;
import com.example.bookshop.dto.category.CreateCategoryRequestDto;
import com.example.bookshop.exception.EntityNotFoundException;
import com.example.bookshop.mapper.CategoryMapper;
import com.example.bookshop.model.Category;
import com.example.bookshop.repository.CategoryRepository;
import com.example.bookshop.service.impl.CategoryServiceImpl;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

@ExtendWith(MockitoExtension.class)
public class CategoryServiceTest {
    private static final String CATEGORY_NAME = "Category Name";
    @Mock
    private CategoryRepository categoryRepository;
    @Mock
    private CategoryMapper categoryMapper;
    @InjectMocks
    private CategoryServiceImpl categoryService;

    @Test
    @DisplayName("Verify save() method works")
    public void save_WithValidCreateCategoryRequestDto_ReturnValidCategoryDto() {
        CreateCategoryRequestDto requestDto = new CreateCategoryRequestDto();
        requestDto.setName(CATEGORY_NAME);
        Category category = new Category();
        category.setName(CATEGORY_NAME);
        CategoryDto categoryDto = new CategoryDto();
        categoryDto.setName(CATEGORY_NAME);
        Mockito.when(categoryRepository.save(category)).thenReturn(category);
        Mockito.when(categoryMapper.toDto(category)).thenReturn(categoryDto);
        Mockito.when(categoryMapper.toModel(requestDto)).thenReturn(category);
        CategoryDto savedBookDto = categoryService.save(requestDto);
        assertThat(savedBookDto).isEqualTo(categoryDto);
        verify(categoryRepository).save(category);
        verify(categoryMapper).toModel(requestDto);
        verify(categoryMapper).toDto(category);
        verifyNoMoreInteractions(categoryRepository, categoryMapper);
    }

    @Test
    @DisplayName("Verify findAll() methods works")
    public void findAll_ValidPageable_ReturnAllCategoriesDto() {
        Long categoryId = 1L;
        Category category = new Category();
        category.setId(categoryId);
        category.setName(CATEGORY_NAME);
        CategoryDto categoryDto = new CategoryDto();
        categoryDto.setId(categoryId);
        categoryDto.setName(CATEGORY_NAME);
        Pageable pageable = PageRequest.of(0, 10);
        List<Category> categories = List.of(category);
        Page<Category> categoriesPage = new PageImpl<>(categories, pageable, categories.size());
        when(categoryRepository.findAll(pageable)).thenReturn(categoriesPage);
        when(categoryMapper.toDto(category)).thenReturn(categoryDto);
        List<CategoryDto> categoryDtos = categoryService.findAll(pageable);
        assertThat(categoryDtos).hasSize(1);
        assertThat(categoryDtos.get(0)).isEqualTo(categoryDto);
        verify(categoryRepository).findAll(pageable);
        verify(categoryMapper).toDto(category);
        verifyNoMoreInteractions(categoryRepository, categoryMapper);
    }

    @Test
    @DisplayName("Verify findById() method works")
    public void findById_WithValidCategoryId_ReturnCategoryDto() {
        Long categoryId = 1L;
        Category category = new Category();
        category.setId(categoryId);
        category.setName(CATEGORY_NAME);
        CategoryDto expected = new CategoryDto();
        expected.setId(categoryId);
        expected.setName(CATEGORY_NAME);
        when(categoryRepository.findById(categoryId)).thenReturn(Optional.of(category));
        when(categoryMapper.toDto(category)).thenReturn(expected);
        CategoryDto actual = categoryService.findById(categoryId);
        assertThat(expected).isEqualTo(actual);
        verify(categoryRepository).findById(categoryId);
        verify(categoryMapper).toDto(category);
        verifyNoMoreInteractions(categoryRepository, categoryMapper);
    }

    @Test
    @DisplayName(
            "Verify that an exception is thrown "
            + "when trying to find a non-existent Category by the given ID"
    )
    public void findById_WithNonExistingCategoryId_ThrowsException() {
        Long categoryId = 122L;
        when(categoryRepository.findById(categoryId)).thenReturn(Optional.empty());
        Exception exception = assertThrows(
                EntityNotFoundException.class,
                () -> categoryService.findById(categoryId)
        );
        String expected = "Can't find category with id: " + categoryId;
        String actual = exception.getMessage();
        assertThat(actual).isEqualTo(expected);
        verify(categoryRepository).findById(categoryId);
        verifyNoMoreInteractions(categoryRepository);
    }
}
