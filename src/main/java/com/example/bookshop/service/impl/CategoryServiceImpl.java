package com.example.bookshop.service.impl;

import com.example.bookshop.dto.book.BookWithoutCategoriesDto;
import com.example.bookshop.dto.category.CategoryDto;
import com.example.bookshop.dto.category.CreateCategoryRequestDto;
import com.example.bookshop.exception.EntityNotFoundException;
import com.example.bookshop.mapper.BookMapper;
import com.example.bookshop.mapper.CategoryMapper;
import com.example.bookshop.model.Category;
import com.example.bookshop.repository.BookRepository;
import com.example.bookshop.repository.CategoryRepository;
import com.example.bookshop.service.CategoryService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;
    private final BookRepository bookRepository;
    private final CategoryMapper categoryMapper;
    private final BookMapper bookMapper;

    @Override
    public CategoryDto save(CreateCategoryRequestDto requestDto) {
        Category category = categoryMapper.toModel(requestDto);
        return categoryMapper.toDto(
                categoryRepository.save(category)
        );
    }

    @Override
    public List<CategoryDto> getAll() {
        return categoryRepository.findAll()
                .stream()
                .map(categoryMapper::toDto)
                .toList();
    }

    @Override
    public CategoryDto findById(Long id) {
        Category category = categoryRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Can't find category with id: " + id)
        );
        return categoryMapper.toDto(category);
    }

    @Override
    public CategoryDto update(CreateCategoryRequestDto requestDto, Long id) {
        Category category = categoryRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException(
                        "Can't find category with id: "
                                + id
                ));
        categoryMapper.updateCategoryFromDto(category, requestDto);
        return categoryMapper.toDto(categoryRepository.save(category));
    }

    @Override
    public void delete(Long id) {
        categoryRepository.deleteById(id);
    }

    @Override
    public List<BookWithoutCategoriesDto> findBooksByCategoryId(Long id) {
        return bookRepository.findAllByCategories_Id(id)
                .stream()
                .map(bookMapper::toBookWithoutCategoriesDto)
                .toList();
    }
}
