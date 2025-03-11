package com.example.bookshop.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.bookshop.dto.book.BookDto;
import com.example.bookshop.dto.book.CreateBookRequestDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import javax.sql.DataSource;
import lombok.SneakyThrows;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaType;
import org.springframework.jdbc.datasource.init.ScriptUtils;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class BookControllerTest {
    protected static MockMvc mockMvc;
    private static final String BOOK_TITLE_1 = "The Witcher";
    private static final String BOOK_AUTHOR_1 = "Andjez";
    private static final String BOOK_ISBN_1 = "ISBN 1";
    private static final BigDecimal BOOK_PRICE_1 = BigDecimal.valueOf(100.00);
    private static final String BOOK_TITLE_2 = "X and Y";
    private static final String BOOK_AUTHOR_2 = "Agatha";
    private static final String BOOK_ISBN_2 = "ISBN 2";
    private static final BigDecimal BOOK_PRICE_2 = BigDecimal.valueOf(120.00);
    @Autowired
    private ObjectMapper objectMapper;

    @BeforeAll
    static void beforeAll(
            @Autowired DataSource dataSource,
            @Autowired WebApplicationContext applicationContext
    ) throws SQLException {
        mockMvc = MockMvcBuilders.webAppContextSetup(applicationContext)
                .apply(springSecurity())
                .build();
        teardown(dataSource);
        try (Connection connection = dataSource.getConnection()) {
            connection.setAutoCommit(true);
            ScriptUtils.executeSqlScript(
                    connection,
                    new ClassPathResource("database/categories/"
                            + "add-fantasy-category-to-categories-table.sql")
            );
            ScriptUtils.executeSqlScript(
                    connection,
                    new ClassPathResource("database/categories/"
                            + "add-detective-category-to-categories-table.sql")
            );
        }
    }

    @AfterAll
    static void afterAll(
            @Autowired DataSource dataSource
    ) {
        teardown(dataSource);
    }

    @SneakyThrows
    static void teardown(DataSource dataSource) {
        try (Connection connection = dataSource.getConnection()) {
            connection.setAutoCommit(true);
            ScriptUtils.executeSqlScript(
                    connection,
                    new ClassPathResource("database/books-categories/"
                            + "remove-all-books-categories.sql")
            );
            ScriptUtils.executeSqlScript(
                    connection,
                    new ClassPathResource(
                            "database/books/remove-all-books.sql")
            );
            ScriptUtils.executeSqlScript(
                    connection,
                    new ClassPathResource("database/categories/remove-all-categories.sql")
            );
        }
    }

    @WithMockUser(username = "admin", authorities = {"ADMIN"})
    @Test
    @Sql(scripts = {
            "classpath:database/books-categories/remove-all-books-categories.sql",
            "classpath:database/books/remove-all-books.sql"
    }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @DisplayName("Create a new book")
    void createBooks_ValidRequestDto_ReturnsBookDto() throws Exception {
        CreateBookRequestDto createBookRequestDto = new CreateBookRequestDto()
                .setTitle(BOOK_TITLE_1)
                .setAuthor(BOOK_AUTHOR_1)
                .setIsbn(BOOK_ISBN_1)
                .setPrice(BOOK_PRICE_1)
                .setCategoryIds(List.of(1L));
        String jsonRequest = objectMapper.writeValueAsString(createBookRequestDto);
        MvcResult result = mockMvc.perform(
                        post("/books")
                                .content(jsonRequest)
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andReturn();
        BookDto actual = objectMapper.readValue(
                result.getResponse().getContentAsString(), BookDto.class
        );
        assertNotNull(actual);
        assertThat(BOOK_TITLE_1).isEqualTo(actual.getTitle());
        assertThat(BOOK_AUTHOR_1).isEqualTo(actual.getAuthor());
        assertThat(BOOK_PRICE_1.stripTrailingZeros())
                .isEqualTo(actual.getPrice().stripTrailingZeros());
        assertThat(BOOK_ISBN_1).isEqualTo(actual.getIsbn());
        assertThat(Set.of(1L)).isEqualTo(actual.getCategoryIds());
    }

    @WithMockUser(username = "user", authorities = {"USER"})
    @Test
    @Sql(scripts = {
            "classpath:database/books/add-witcher-book-to-books-table.sql",
            "classpath:database/books/add-x-and-y-book-to-books-table.sql",
            "classpath:database/books-categories/add-witcher-is-fantasy-to-books-categories.sql",
            "classpath:database/books-categories/add-x-and-y-is-detective-to-books-categories.sql"
    }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = {
            "classpath:database/books-categories/remove-all-books-categories.sql",
            "classpath:database/books/remove-all-books.sql"
    }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @DisplayName("Find all books")
    void findAllBooks_GivenBooksInCatalog_ReturnAllBooksDto()
            throws Exception {
        MvcResult result = mockMvc.perform(
                        get("/books")
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andReturn();
        List<BookDto> actual = Arrays.stream(objectMapper.readValue(
                result.getResponse().getContentAsByteArray(), BookDto[].class
        )).toList();
        assertNotNull(actual);
        assertEquals(2, actual.size());
        assertThat(actual.get(0).getTitle()).isEqualTo(BOOK_TITLE_1);
        assertThat(actual.get(0).getAuthor()).isEqualTo(BOOK_AUTHOR_1);
        assertThat(actual.get(0).getIsbn()).isEqualTo(BOOK_ISBN_1);
        assertThat(actual.get(0).getPrice().stripTrailingZeros())
                .isEqualTo(BOOK_PRICE_1.stripTrailingZeros());
        assertThat(actual.get(0).getCategoryIds()).isEqualTo(Set.of(1L));
        assertThat(actual.get(1).getTitle()).isEqualTo(BOOK_TITLE_2);
        assertThat(actual.get(1).getAuthor()).isEqualTo(BOOK_AUTHOR_2);
        assertThat(actual.get(1).getIsbn()).isEqualTo(BOOK_ISBN_2);
        assertThat(actual.get(1).getPrice().stripTrailingZeros())
                .isEqualTo(BOOK_PRICE_2.stripTrailingZeros());
        assertThat(actual.get(1).getCategoryIds()).isEqualTo(Set.of(2L));
    }

    @WithMockUser(username = "admin", authorities = {"ADMIN"})
    @Test
    @Sql(scripts = {
            "classpath:database/books/add-witcher-book-to-books-table.sql"
    }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @DisplayName("Delete book by ID")
    void deleteBookById_ValidBookId_ReturnsNoContentStatus() throws Exception {
        mockMvc.perform(
                        delete("/books/{id}", 1L)
                )
                .andExpect(status().isNoContent())
                .andReturn();
    }

    @WithMockUser(username = "user", authorities = {"USER"})
    @Test
    @DisplayName("Find book by invalid ID returns 404 Not Found")
    void findBookById_InvalidId_ReturnsNotFoundStatus() throws Exception {
        mockMvc.perform(
                        get("/books/{id}", 1L)
                )
                .andExpect(status().isNotFound())
                .andReturn();
    }
}
