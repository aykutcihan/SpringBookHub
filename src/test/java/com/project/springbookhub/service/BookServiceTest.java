package com.project.springbookhub.service;

import com.project.springbookhub.exception.BookNotFoundException;
import com.project.springbookhub.model.concretes.Book;
import com.project.springbookhub.payload.ResponseMessage;
import com.project.springbookhub.payload.mapper.BookDto;
import com.project.springbookhub.payload.request.BookRequest;
import com.project.springbookhub.payload.response.BookResponse;
import com.project.springbookhub.repository.BookRepository;
import com.project.springbookhub.util.ServiceHelpers;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.junit.jupiter.api.*;
import org.springframework.data.domain.*;
import org.springframework.http.HttpStatus;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;


import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class BookServiceTest {
    @Mock
    private BookDto bookDto;
    @Mock
    private ServiceHelpers serviceHelpers;

    @Mock
    private BookRepository bookRepository;
    @InjectMocks
    private BookService bookService;

    private BookRequest bookRequest;
    private Book book;
    private BookResponse bookResponse;


    @BeforeEach
    void setUp() {
        bookRequest = BookRequest.builder()
                .title("Mahrem")
                .author("Elif Shafak")
                .price(BigDecimal.valueOf(9.90))
                .description("Story of four people")
                .stockQuantity(300)
                .build();

        book = Book.builder()
                .id(1L)
                .title(bookRequest.getTitle())
                .author(bookRequest.getAuthor())
                .price(bookRequest.getPrice())
                .description(bookRequest.getDescription())
                .stockQuantity(bookRequest.getStockQuantity())
                .build();

        bookResponse = BookResponse.builder()
                .id(book.getId())
                .title(book.getTitle())
                .author(book.getAuthor())
                .price(book.getPrice())
                .description(book.getDescription())
                .stockQuantity(book.getStockQuantity())
                .build();

    }

    @Test
    void createBookTest() {

        // Arrange
        when(bookDto.mapBookRequestToBook(bookRequest)).thenReturn(book);
        when(bookRepository.save(any(Book.class))).thenReturn(book);
        when(bookDto.mapBookToBookResponse(any(Book.class))).thenReturn(bookResponse);

        // Act
        ResponseMessage<BookResponse> response = bookService.createBook(bookRequest);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.CREATED, response.getHttpStatus());
        assertEquals("Book Created Successfully", response.getMessage());
        assertNotNull(response.getObject());
        assertEquals(bookResponse, response.getObject());

        // Verify the interactions
        verify(bookDto).mapBookRequestToBook(bookRequest);
        verify(bookRepository).save(book);
        verify(bookDto).mapBookToBookResponse(book);
    }

    @Test
    void getBookByIdTest() {
        // Arrange
        Long bookId = 1L;
        when(bookRepository.findById(bookId)).thenReturn(Optional.of(book));
        when(bookDto.mapBookToBookResponse(book)).thenReturn(bookResponse);

        // Act
        ResponseMessage<BookResponse> response = bookService.getBookById(bookId);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getHttpStatus());
        assertEquals("Book found", response.getMessage());
        assertEquals(bookResponse, response.getObject());

        // Verify the interactions
        verify(bookRepository).findById(bookId);
        verify(bookDto).mapBookToBookResponse(book);
    }

    @Test
    void getBookByIdNotFoundTest() {
        // Arrange
        Long bookId = 1L;
        when(bookRepository.findById(bookId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(BookNotFoundException.class, () -> {
            bookService.getBookById(bookId);
        });

        // Verify the interaction
        verify(bookRepository).findById(bookId);
    }


    @Test
    void getAllBooksFilteredByAuthorAndTitleTest() {
        // Arrange
        String author = "Elif Shafak";
        String title = "Mahrem";
        List<Book> books = Arrays.asList(book);
        when(bookRepository.findByAuthorAndTitle(author, title)).thenReturn(books);
        when(bookDto.mapBookToBookResponse(any(Book.class))).thenReturn(bookResponse);

        // Act
        List<BookResponse> responses = bookService.getAllBooksFilteredByAuthorAndTitle(author, title);

        // Assert
        assertNotNull(responses);
        assertFalse(responses.isEmpty());
        assertEquals(books.size(), responses.size());
        assertEquals(bookResponse, responses.get(0)); // İlk elemanın doğru olduğunu kontrol et

        // Verify the interactions
        verify(bookRepository).findByAuthorAndTitle(author, title);
        verify(bookDto, times(books.size())).mapBookToBookResponse(any(Book.class));
    }

    @Test
    void getAllBooksFilteredByAuthorOnlyTest() {
        // Arrange
        String author = "Elif Shafak";
        List<Book> books = Arrays.asList(book);
        when(bookRepository.findByAuthor(author)).thenReturn(books);
        when(bookDto.mapBookToBookResponse(any(Book.class))).thenReturn(bookResponse);

        // Act
        List<BookResponse> responses = bookService.getAllBooksFilteredByAuthorAndTitle(author, null);

        // Assert
        assertNotNull(responses);
        assertFalse(responses.isEmpty());
        assertEquals(books.size(), responses.size());
        assertEquals(bookResponse, responses.get(0));

        // Verify the interactions
        verify(bookRepository).findByAuthor(author);
        verify(bookDto, times(books.size())).mapBookToBookResponse(any(Book.class));
    }

    @Test
    void getAllBooksFilteredByTitleOnlyTest() {
        // Arrange
        String title = "Mahrem";
        List<Book> books = Arrays.asList(book);
        when(bookRepository.findByTitle(title)).thenReturn(books);
        when(bookDto.mapBookToBookResponse(any(Book.class))).thenReturn(bookResponse);

        // Act
        List<BookResponse> responses = bookService.getAllBooksFilteredByAuthorAndTitle(null, title);

        // Assert
        assertNotNull(responses);
        assertFalse(responses.isEmpty());
        assertEquals(books.size(), responses.size());
        assertEquals(bookResponse, responses.get(0));

        // Verify the interactions
        verify(bookRepository).findByTitle(title);
        verify(bookDto, times(books.size())).mapBookToBookResponse(any(Book.class));
    }

    @Test
    void getAllBooksTest() {
        // Arrange
        List<Book> books = Arrays.asList(book);
        when(bookRepository.findAll()).thenReturn(books);
        when(bookDto.mapBookToBookResponse(any(Book.class))).thenReturn(bookResponse);

        // Act
        List<BookResponse> responses = bookService.getAllBooksFilteredByAuthorAndTitle(null, null);

        // Assert
        assertNotNull(responses);
        assertFalse(responses.isEmpty());
        assertEquals(books.size(), responses.size());
        assertEquals(bookResponse, responses.get(0));

        // Verify the interactions
        verify(bookRepository).findAll();
        verify(bookDto, times(books.size())).mapBookToBookResponse(any(Book.class));
    }

    @Test
    void findBookByPageTest() {

        // Arrange
        int page = 0;
        int size = 5;
        String sort = "title";
        String type = "asc";
        Page<Book> bookPage = new PageImpl<>(Arrays.asList(book));

        when(serviceHelpers.getPageableWithProperties(page, size, sort, type)).thenReturn(PageRequest.of(page, size, Sort.by(Sort.Direction.ASC, sort)));
        when(bookRepository.findAll(any(Pageable.class))).thenReturn(bookPage);
        when(bookDto.mapBookToBookResponse(any(Book.class))).thenReturn(bookResponse);

        // Act
        Page<BookResponse> response = bookService.findBookByPage(page, size, sort, type);

        // Assert
        assertNotNull(response);
        assertEquals(bookPage.getTotalElements(), response.getTotalElements());
        assertEquals(1, response.getContent().size());
        assertEquals(bookResponse, response.getContent().get(0));

        // Verify the interactions
        verify(bookRepository).findAll(any(Pageable.class));
        verify(bookDto).mapBookToBookResponse(any(Book.class));
    }

    @Test
    void updateBookTest() {

        // Arrange
        Long bookId = 1L;
        when(bookRepository.findById(bookId)).thenReturn(Optional.of(book));
        when(bookRepository.save(any(Book.class))).thenReturn(book);
        when(bookDto.mapBookToBookResponse(any(Book.class))).thenReturn(bookResponse);

        // Act
        ResponseMessage<BookResponse> response = bookService.updateBook(bookId, bookRequest);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getHttpStatus());
        assertEquals("Book updated successfully", response.getMessage());
        assertEquals(bookResponse, response.getObject());

        // Verify the interactions
        verify(bookRepository).findById(bookId);
        verify(bookRepository).save(book);
        verify(bookDto).mapBookToBookResponse(book);
    }

    @Test
    void updateBookNotFoundTest() {
        // Arrange
        Long bookId = 1L;
        when(bookRepository.findById(bookId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(BookNotFoundException.class, () -> {
            bookService.updateBook(bookId, bookRequest);
        });

        // Verify the interaction
        verify(bookRepository).findById(bookId);
    }

    @Test
    void deleteBookTest() {
        // Arrange
        Long bookId = 1L;
        when(bookRepository.findById(bookId)).thenReturn(Optional.of(book));
        doNothing().when(bookRepository).deleteById(bookId);

        // Act
        ResponseMessage<?> response = bookService.deleteBook(bookId);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getHttpStatus());
        assertEquals("Book deleted successfully", response.getMessage());

        // Verify the interactions
        verify(bookRepository).findById(bookId);
        verify(bookRepository).deleteById(bookId);
    }

    @Test
    void deleteBookNotFoundTest() {
        // Arrange
        Long bookId = 1L;
        when(bookRepository.findById(bookId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(BookNotFoundException.class, () -> {
            bookService.deleteBook(bookId);
        });

        // Verify the interaction
        verify(bookRepository).findById(bookId);
    }



    }


