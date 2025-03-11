package com.example.bookshop.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

import com.example.bookshop.model.Book;
import java.math.BigDecimal;
import java.util.List;
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
    private static final String BOOK_ISBN = "ISBN 1";
    private static final BigDecimal BOOK_PRICE = BigDecimal.valueOf(100.00);

    @Autowired
    private BookRepository bookRepository;

    @Test
    @DisplayName("Find all books by category id")
    @Sql(scripts = {
            "classpath:database/categories/add-fantasy-category-to-categories-table.sql",
            "classpath:database/books/add-witcher-book-to-books-table.sql",
            "classpath:database/books-categories/add-witcher-is-fantasy-to-books-categories.sql"
    }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = {
            "classpath:database/books-categories/remove-all-books-categories.sql",
            "classpath:database/books/remove-all-books.sql",
            "classpath:database/categories/remove-all-categories.sql"
    }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void findAllByCategoriesId_WithValidCategoriesId_ReturnsBooks() {
        Pageable pageable = PageRequest.of(0, 10);
        List<Book> actual = bookRepository.findAllByCategoriesId(1L, pageable);
        assertEquals(1, actual.size());
        assertThat(actual.get(0).getTitle()).isEqualTo(BOOK_TITLE);
        assertThat(actual.get(0).getAuthor()).isEqualTo(BOOK_AUTHOR);
        assertThat(actual.get(0).getPrice().stripTrailingZeros())
                .isEqualTo(BOOK_PRICE.stripTrailingZeros());
        assertThat(actual.get(0).getIsbn()).isEqualTo(BOOK_ISBN);
    }
}
