package com.project.springbookhub.repository;

        import com.project.springbookhub.model.concretes.Book;
        import org.junit.jupiter.api.BeforeEach;
        import org.junit.jupiter.api.Test;
        import org.springframework.beans.factory.annotation.Autowired;
        import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
        import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

        import java.math.BigDecimal;
        import java.util.List;

        import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class BookRepositoryTests {
    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private BookRepository bookRepository;

    @BeforeEach
    void setUp() {
        // Arrange
        Book book1 = Book.builder()
                .title("Title1")
                .author("Author1")
                .price(new BigDecimal("19.99"))
                .description("Description1")
                .stockQuantity(10)
                .build();

        Book book2 = Book.builder()
                .title("Title2")
                .author("Author2")
                .price(new BigDecimal("29.99"))
                .description("Description2")
                .stockQuantity(5)
                .build();

        Book book3 = Book.builder()
                .title("Title3")
                .author("Author1")
                .price(new BigDecimal("39.99"))
                .description("Description3")
                .stockQuantity(15)
                .build();

        entityManager.persist(book1);
        entityManager.persist(book2);
        entityManager.persist(book3);
    }

    @Test
    void whenFindByAuthor_thenReturnBooks() {
        // Act
        List<Book> books = bookRepository.findByAuthor("Author1");

        // Assert
        assertThat(books).hasSize(2);
        assertThat(books).extracting(Book::getAuthor).containsOnly("Author1");
    }

    @Test
    void whenFindByTitle_thenReturnBooks() {
        // Act
        List<Book> books = bookRepository.findByTitle("Title2");

        // Assert
        assertThat(books).hasSize(1);
        assertThat(books.get(0).getTitle()).isEqualTo("Title2");
    }

    @Test
    void whenFindByAuthorAndTitle_thenReturnBooks() {
        // Act
        List<Book> books = bookRepository.findByAuthorAndTitle("Author1", "Title3");

        // Assert
        assertThat(books).hasSize(1);
        assertThat(books.get(0).getAuthor()).isEqualTo("Author1");
        assertThat(books.get(0).getTitle()).isEqualTo("Title3");
    }

    @Test
    void whenFindByAuthor_thenReturnEmptyIfNoMatch() {
        // Act
        List<Book> books = bookRepository.findByAuthor("NonExistingAuthor");

        // Assert
        assertThat(books).isEmpty();
    }

    @Test
    void whenFindByTitle_thenReturnEmptyIfNoMatch() {
        // Act
        List<Book> books = bookRepository.findByTitle("NonExistingTitle");

        // Assert
        assertThat(books).isEmpty();
    }

    @Test
    void whenFindByAuthorAndTitle_thenReturnEmptyIfNoMatch() {
        // Act
        List<Book> books = bookRepository.findByAuthorAndTitle("NonExistingAuthor", "NonExistingTitle");

        // Assert
        assertThat(books).isEmpty();
    }
}

