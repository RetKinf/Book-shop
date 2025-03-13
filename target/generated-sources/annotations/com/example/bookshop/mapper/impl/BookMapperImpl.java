package com.example.bookshop.mapper.impl;

import com.example.bookshop.dto.book.BookDto;
import com.example.bookshop.dto.book.BookWithoutCategoriesDto;
import com.example.bookshop.dto.book.CreateBookRequestDto;
import com.example.bookshop.mapper.BookMapper;
import com.example.bookshop.model.Book;
import java.math.BigDecimal;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-03-12T12:11:32+0200",
    comments = "version: 1.6.3, compiler: javac, environment: Java 23.0.2 (Oracle Corporation)"
)
@Component
public class BookMapperImpl implements BookMapper {

    @Override
    public BookDto toDto(Book book) {
        if ( book == null ) {
            return null;
        }

        BookDto bookDto = new BookDto();

        if ( book.getId() != null ) {
            bookDto.setId( book.getId() );
        }
        if ( book.getTitle() != null ) {
            bookDto.setTitle( book.getTitle() );
        }
        if ( book.getAuthor() != null ) {
            bookDto.setAuthor( book.getAuthor() );
        }
        if ( book.getIsbn() != null ) {
            bookDto.setIsbn( book.getIsbn() );
        }
        if ( book.getPrice() != null ) {
            bookDto.setPrice( book.getPrice() );
        }
        if ( book.getDescription() != null ) {
            bookDto.setDescription( book.getDescription() );
        }
        if ( book.getCoverImage() != null ) {
            bookDto.setCoverImage( book.getCoverImage() );
        }

        setCategoryIds( bookDto, book );

        return bookDto;
    }

    @Override
    public BookWithoutCategoriesDto toBookWithoutCategoriesDto(Book book) {
        if ( book == null ) {
            return null;
        }

        Long id = null;
        String title = null;
        String author = null;
        String isbn = null;
        BigDecimal price = null;
        String description = null;
        String coverImage = null;

        if ( book.getId() != null ) {
            id = book.getId();
        }
        if ( book.getTitle() != null ) {
            title = book.getTitle();
        }
        if ( book.getAuthor() != null ) {
            author = book.getAuthor();
        }
        if ( book.getIsbn() != null ) {
            isbn = book.getIsbn();
        }
        if ( book.getPrice() != null ) {
            price = book.getPrice();
        }
        if ( book.getDescription() != null ) {
            description = book.getDescription();
        }
        if ( book.getCoverImage() != null ) {
            coverImage = book.getCoverImage();
        }

        BookWithoutCategoriesDto bookWithoutCategoriesDto = new BookWithoutCategoriesDto( id, title, author, isbn, price, description, coverImage );

        return bookWithoutCategoriesDto;
    }

    @Override
    public Book toModel(CreateBookRequestDto requestDto) {
        if ( requestDto == null ) {
            return null;
        }

        Book book = new Book();

        if ( requestDto.getTitle() != null ) {
            book.setTitle( requestDto.getTitle() );
        }
        if ( requestDto.getAuthor() != null ) {
            book.setAuthor( requestDto.getAuthor() );
        }
        if ( requestDto.getIsbn() != null ) {
            book.setIsbn( requestDto.getIsbn() );
        }
        if ( requestDto.getPrice() != null ) {
            book.setPrice( requestDto.getPrice() );
        }
        if ( requestDto.getDescription() != null ) {
            book.setDescription( requestDto.getDescription() );
        }
        if ( requestDto.getCoverImage() != null ) {
            book.setCoverImage( requestDto.getCoverImage() );
        }

        return book;
    }

    @Override
    public void updateBookFromDto(Book book, CreateBookRequestDto requestDto) {
        if ( requestDto == null ) {
            return;
        }

        if ( requestDto.getTitle() != null ) {
            book.setTitle( requestDto.getTitle() );
        }
        else {
            book.setTitle( null );
        }
        if ( requestDto.getAuthor() != null ) {
            book.setAuthor( requestDto.getAuthor() );
        }
        else {
            book.setAuthor( null );
        }
        if ( requestDto.getIsbn() != null ) {
            book.setIsbn( requestDto.getIsbn() );
        }
        else {
            book.setIsbn( null );
        }
        if ( requestDto.getPrice() != null ) {
            book.setPrice( requestDto.getPrice() );
        }
        else {
            book.setPrice( null );
        }
        if ( requestDto.getDescription() != null ) {
            book.setDescription( requestDto.getDescription() );
        }
        else {
            book.setDescription( null );
        }
        if ( requestDto.getCoverImage() != null ) {
            book.setCoverImage( requestDto.getCoverImage() );
        }
        else {
            book.setCoverImage( null );
        }
    }
}
