package com.example.bookshop.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.example.bookshop.model.Book;
import java.math.BigDecimal;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.jdbc.Sql;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class BookRepositoryTest {
    private static final String BOOK_TITLE = "The Witcher";
    private static final String BOOK_AUTHOR = "Andjez";
    private static final String BOOK_ISBN = "isbn";
    private static final BigDecimal BOOK_PRICE = BigDecimal.valueOf(50.12);

    @Autowired
    private BookRepository bookRepository;

    @Test
    @DisplayName("1")
    @Sql(scripts = {
            "classpath:database/categories/add-fantasy-category-to-categories-table.sql",
            "classpath:database/books/add-witcher-book-to-books-table.sql",
            "classpath:database/books-categories/add-witcher-is-fantasy-to-books-categories.sql"
    }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = {
            "classpath:database/books-categories/"
                    + "remove-witcher-is-fantasy-from-books-categories.sql",
            "classpath:database/books/remove-witcher-book-from-books-table.sql",
            "classpath:database/categories/remove-fantasy-category-from-categories-table.sql"
    }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void findAllByCategoriesId_WithValidCategoriesId_ReturnsBooks() {
        Pageable pageable = PageRequest.of(0, 10);
        List<Book> actual = bookRepository.findAllByCategoriesId(1L, pageable);
        Assertions.assertEquals(1, actual.size());
        assertEquals(BOOK_TITLE, actual.get(0).getTitle());
        assertEquals(BOOK_AUTHOR, actual.get(0).getAuthor());
        assertEquals(BOOK_PRICE, actual.get(0).getPrice());
        assertEquals(BOOK_ISBN, actual.get(0).getIsbn());
    }
}
