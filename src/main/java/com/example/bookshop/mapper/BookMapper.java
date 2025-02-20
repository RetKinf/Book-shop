package com.example.bookshop.mapper;

import com.example.bookshop.config.MapperConfig;
import com.example.bookshop.model.Book;
import com.example.bookshop.model.dto.BookDto;
import com.example.bookshop.model.dto.CreateBookRequestDto;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(config = MapperConfig.class)
public interface BookMapper {
    BookDto toDto(Book book);

    Book toModel(CreateBookRequestDto requestDto);

    void updateBookFromDto(@MappingTarget Book book, CreateBookRequestDto requestDto);
}
