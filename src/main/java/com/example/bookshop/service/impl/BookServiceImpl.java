package com.example.bookshop.service.impl;

import com.example.bookshop.dto.book.BookDto;
import com.example.bookshop.dto.book.CreateBookRequestDto;
import com.example.bookshop.exception.EntityNotFoundException;
import com.example.bookshop.mapper.BookMapper;
import com.example.bookshop.model.Book;
import com.example.bookshop.model.Category;
import com.example.bookshop.repository.BookRepository;
import com.example.bookshop.repository.CategoryRepository;
import com.example.bookshop.service.BookService;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {
    private final BookRepository bookRepository;
    private final BookMapper bookMapper;
    private final CategoryRepository categoryRepository;

    @Override
    public BookDto save(CreateBookRequestDto requestDto) {
        Book book = bookMapper.toModel(requestDto);
        if (requestDto.getCategoryIds() != null) {
            Set<Category> categories = requestDto.getCategoryIds()
                    .stream()
                    .map(id -> categoryRepository.findById(id).orElseThrow(
                            () -> new EntityNotFoundException(
                                    "Category with id "
                                            + id + " not found"
                            ))).collect(Collectors.toSet());
            book.setCategories(categories);
        }
        return bookMapper.toDto(bookRepository.save(book));
    }

    @Override
    public List<BookDto> findAll(Pageable pageable) {
        return bookRepository.findAll(pageable)
                .stream()
                .map(bookMapper::toDto)
                .toList();
    }

    @Override
    public BookDto findById(Long id) {
        Book book = bookRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Can't find book by ID " + id));
        return bookMapper.toDto(book);
    }

    @Override
    public BookDto update(CreateBookRequestDto requestDto, long id) {
        Book book = bookRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Can't find book by ID " + id)
        );
        bookMapper.updateBookFromDto(book, requestDto);
        return bookMapper.toDto(bookRepository.save(book));
    }

    @Override
    public void deleteById(Long id) {
        bookRepository.deleteById(id);
    }
}
