package com.example.bookshop.mapper;

import com.example.bookshop.config.MapperConfig;
import com.example.bookshop.dto.book.BookDto;
import com.example.bookshop.dto.book.BookWithoutCategoriesDto;
import com.example.bookshop.dto.book.CreateBookRequestDto;
import com.example.bookshop.model.Book;
import com.example.bookshop.model.Category;
import java.util.Set;
import java.util.stream.Collectors;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(config = MapperConfig.class)
public interface BookMapper {
    @Mapping(target = "categoryIds", ignore = true)
    BookDto toDto(Book book);

    @AfterMapping
    default void setCategoryIds(@MappingTarget BookDto bookDto, Book book) {
        Set<Long> categoryIds = book.getCategories()
                .stream()
                .map(Category::getId)
                .collect(Collectors.toSet());
        bookDto.setCategoryIds(categoryIds);
    }

    BookWithoutCategoriesDto toBookWithoutCategoriesDto(Book book);

    Book toModel(CreateBookRequestDto requestDto);

    void updateBookFromDto(@MappingTarget Book book, CreateBookRequestDto requestDto);
}
