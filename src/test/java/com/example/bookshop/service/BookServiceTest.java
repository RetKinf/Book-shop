package com.example.bookshop.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import com.example.bookshop.dto.book.BookDto;
import com.example.bookshop.dto.book.CreateBookRequestDto;
import com.example.bookshop.exception.EntityNotFoundException;
import com.example.bookshop.mapper.BookMapper;
import com.example.bookshop.model.Book;
import com.example.bookshop.repository.BookRepository;
import com.example.bookshop.service.impl.BookServiceImpl;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

@ExtendWith(MockitoExtension.class)
public class BookServiceTest {
    private static final String BOOK_TITLE = "Book title";
    private static final String BOOK_AUTHOR = "Author";
    private static final String BOOK_ISBN = "ISBN";
    private static final BigDecimal BOOK_PRICE = BigDecimal.valueOf(10.99);
    @Mock
    private BookRepository bookRepository;
    @Mock
    private BookMapper bookMapper;
    @InjectMocks
    private BookServiceImpl bookService;

    @Test
    @DisplayName("Verify save() method works")
    public void save_WithValidCreateBookRequestDto_ReturnValidBookDto() {
        CreateBookRequestDto requestDto = new CreateBookRequestDto();
        requestDto.setTitle(BOOK_TITLE);
        requestDto.setAuthor(BOOK_AUTHOR);
        requestDto.setIsbn(BOOK_ISBN);
        requestDto.setPrice(BOOK_PRICE);

        Book book = new Book();
        book.setTitle(BOOK_TITLE);
        book.setAuthor(BOOK_AUTHOR);
        book.setIsbn(BOOK_ISBN);
        book.setPrice(BOOK_PRICE);

        BookDto bookDto = new BookDto();
        bookDto.setTitle(BOOK_TITLE);
        bookDto.setAuthor(BOOK_AUTHOR);
        bookDto.setIsbn(BOOK_ISBN);
        bookDto.setPrice(BOOK_PRICE);

        when(bookMapper.toModel(requestDto)).thenReturn(book);
        when(bookRepository.save(book)).thenReturn(book);
        when(bookMapper.toDto(book)).thenReturn(bookDto);

        BookDto savedBookDto = bookService.save(requestDto);
        assertThat(savedBookDto).isEqualTo(bookDto);
        verify(bookRepository, times(1)).save(book);
        verify(bookMapper, times(1)).toModel(requestDto);
        verify(bookMapper, times(1)).toDto(book);
        verifyNoMoreInteractions(bookRepository, bookMapper);
    }

    @Test
    @DisplayName("""
            """)
    public void findById_WithValidBookId_ReturnBookDto() {
        Long bookId = 1L;
        Book book = new Book();
        book.setId(bookId);
        book.setTitle(BOOK_TITLE);
        book.setAuthor(BOOK_AUTHOR);
        book.setIsbn(BOOK_ISBN);
        book.setPrice(BOOK_PRICE);
        when(bookRepository.findById(bookId)).thenReturn(Optional.of(book));
        BookDto actual = bookService.findById(bookId);
        BookDto expected = bookMapper.toDto(book);
        assertEquals(expected, actual);
        verify(bookRepository, times(1)).findById(bookId);
        verify(bookMapper, times(2)).toDto(book);
        verifyNoMoreInteractions(bookRepository, bookMapper);
    }

    @Test
    @DisplayName("""
            Verify that an exception is thrown when trying to find an existing Book by the given id
            """)
    public void findById_WithNonExistingBookId_ThrowException() {
        Long bookId = 200L;
        when(bookRepository.findById(bookId)).thenReturn(Optional.empty());
        Exception exception = assertThrows(
                EntityNotFoundException.class,
                () -> bookService.findById(bookId)
        );
        String expected = "Can't find book by ID " + bookId;
        String actual = exception.getMessage();
        assertEquals(expected, actual);
        verify(bookRepository, times(1)).findById(bookId);
        verifyNoMoreInteractions(bookRepository);
    }

    @Test
    @DisplayName("Verify findAll method")
    public void findAll_ValidPageable_ReturnsAllBookDtos() {
        Long bookId = 1L;
        Book book = new Book();
        book.setId(bookId);
        book.setTitle(BOOK_TITLE);
        book.setAuthor(BOOK_AUTHOR);
        book.setIsbn(BOOK_ISBN);
        book.setPrice(BOOK_PRICE);

        BookDto bookDto = new BookDto();
        bookDto.setId(bookId);
        bookDto.setTitle(BOOK_TITLE);
        bookDto.setAuthor(BOOK_AUTHOR);
        bookDto.setIsbn(BOOK_ISBN);
        bookDto.setPrice(BOOK_PRICE);

        Pageable pageable = PageRequest.of(0, 10);
        List<Book> books = List.of(book);
        Page<Book> booksPage = new PageImpl<>(books, pageable, books.size());

        when(bookRepository.findAll(pageable)).thenReturn(booksPage);
        when(bookMapper.toDto(book)).thenReturn(bookDto);

        List<BookDto> booksDtos = bookService.findAll(pageable);

        assertThat(booksDtos).hasSize(1);
        assertThat(booksDtos.get(0)).isEqualTo(bookDto);

        verify(bookRepository, times(1)).findAll(pageable);
        verify(bookMapper, times(1)).toDto(book);
        verifyNoMoreInteractions(bookRepository, bookMapper);
    }
}
