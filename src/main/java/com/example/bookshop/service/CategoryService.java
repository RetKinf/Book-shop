package com.example.bookshop.service;

import com.example.bookshop.dto.book.BookWithoutCategoriesDto;
import com.example.bookshop.dto.category.CategoryDto;
import com.example.bookshop.dto.category.CreateCategoryRequestDto;
import java.util.List;
import org.springframework.data.domain.Pageable;

public interface CategoryService {
    CategoryDto save(CreateCategoryRequestDto requestDto);

    List<CategoryDto> getAll(Pageable pageable);

    CategoryDto findById(Long id);

    CategoryDto update(CreateCategoryRequestDto requestDto, Long id);

    void delete(Long id);

    List<BookWithoutCategoriesDto> findBooksByCategoryId(Long id, Pageable pageable);
}
