package com.example.bookshop.controller;

import com.example.bookshop.model.dto.BookDto;
import com.example.bookshop.model.dto.CreateBookRequestDto;
import com.example.bookshop.service.BookService;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/books")
public class BookController {
    private final BookService bookService;

    @GetMapping
    public List<BookDto> findAll() {
        return bookService.findAll();
    }

    @GetMapping("/{id}")
    public BookDto findById(@PathVariable long id) {
        return bookService.findById(id);
    }

    @PostMapping
    public BookDto save(@RequestBody @Valid CreateBookRequestDto requestDto) {
        return bookService.save(requestDto);
    }

    @PutMapping("{id}")
    public BookDto update(@PathVariable long id, @RequestBody CreateBookRequestDto requestDto) {
        return bookService.update(requestDto, id);
    }

    @DeleteMapping("{id}")
    public void delete(@PathVariable long id) {
        bookService.deleteById(id);
    }
}
