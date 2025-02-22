package com.example.bookshop.service;

import com.example.bookshop.model.dto.BookDto;
import com.example.bookshop.model.dto.CreateBookRequestDto;
import java.util.List;
import org.springframework.data.domain.Pageable;

public interface BookService {
    BookDto save(CreateBookRequestDto requestDto);

    List<BookDto> findAll(Pageable pageable);

    BookDto findById(Long id);

    BookDto update(CreateBookRequestDto requestDto, long id);

    void deleteById(Long id);
}
