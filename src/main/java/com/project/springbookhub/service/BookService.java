package com.project.springbookhub.service;

import com.project.springbookhub.exception.BookNotFoundException;
import com.project.springbookhub.model.concretes.Book;
import com.project.springbookhub.payload.ResponseMessage;
import com.project.springbookhub.payload.mapper.BookDto;
import com.project.springbookhub.payload.request.BookRequest;
import com.project.springbookhub.payload.response.BookResponse;
import com.project.springbookhub.repository.BookRepository;
import com.project.springbookhub.util.Messages;
import com.project.springbookhub.util.ServiceHelpers;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class BookService {

    private final BookRepository bookRepository;
    private final BookDto bookDto;
    private final EmailService emailService;
    @Value("${app.admin.email}")
    private String adminEmail;

    private final ServiceHelpers serviceHelpers;

    // Creates a new book and saves it to the repository
    public ResponseMessage<BookResponse> createBook(BookRequest bookRequest) {
        Book book = bookRepository.save(bookDto.mapBookRequestToBook(bookRequest));
        BookResponse bookResponse = bookDto.mapBookToBookResponse(book);

        return ResponseMessage.<BookResponse>builder()
                .object(bookResponse)
                .message("Book Created Successfully")
                .httpStatus(HttpStatus.CREATED)
                .build();
    }

    // Retrieves a book by its ID
    public ResponseMessage<BookResponse> getBookById(Long id) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new BookNotFoundException(String.format(Messages.BOOK_NOT_FOUND, id)));

        BookResponse bookResponse = bookDto.mapBookToBookResponse(book);

        return ResponseMessage.<BookResponse>builder()
                .object(bookResponse)
                .message("Book found")
                .httpStatus(HttpStatus.OK)
                .build();
    }

    // Retrieves all books, optionally filtered by author and/or title
    public List<BookResponse> getAllBooksFilteredByAuthorAndTitle(String author, String title) {
        List<Book> books;
        if (author != null && title != null) {
            books = bookRepository.findByAuthorAndTitle(author, title);
        } else if (author != null) {
            books = bookRepository.findByAuthor(author);
        } else if (title != null) {
            books = bookRepository.findByTitle(title);
        } else {
            books = bookRepository.findAll();
        }
        return books.stream()
                .map(bookDto::mapBookToBookResponse)
                .collect(Collectors.toList());
    }

    // Paginates and retrieves books
    public Page<BookResponse> findBookByPage(int page, int size, String sort, String type) {
        Pageable pageable = serviceHelpers.getPageableWithProperties(page, size, sort, type);
        return bookRepository.findAll(pageable).map(bookDto::mapBookToBookResponse);
    }

    // Updates a book's information
    public ResponseMessage<BookResponse> updateBook(Long id, BookRequest bookRequest) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new BookNotFoundException(String.format(Messages.BOOK_NOT_FOUND, id)));

        BookDto.updateBookFromBookRequest(bookRequest, book);
        Book updatedBook = bookRepository.save(book);
        BookResponse bookResponse = bookDto.mapBookToBookResponse(updatedBook);

        return ResponseMessage.<BookResponse>builder()
                .object(bookResponse)
                .message("Book updated successfully")
                .httpStatus(HttpStatus.OK)
                .build();
    }

    // Deletes a book by its ID
    public ResponseMessage<?> deleteBook(Long id) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new BookNotFoundException(String.format(Messages.BOOK_NOT_FOUND, id)));
        bookRepository.deleteById(book.getId());

        return ResponseMessage.<BookResponse>builder()
                .message("Book deleted successfully")
                .httpStatus(HttpStatus.OK)
                .build();
    }

    // Updates the stock of a book and checks for low stock
    public ResponseMessage<BookResponse> updateBookStock(Long id, int quantity) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new BookNotFoundException(String.format(Messages.BOOK_NOT_FOUND, id)));

        book.setStockQuantity(book.getStockQuantity() - quantity);
        book = bookRepository.save(book);

        checkStockAndNotify(book);

        BookResponse bookResponse = bookDto.mapBookToBookResponse(book);

        return ResponseMessage.<BookResponse>builder()
                .object(bookResponse)
                .message("Stock updated successfully for book ID: " + id)
                .httpStatus(HttpStatus.OK)
                .build();
    }

    // Checks if the stock is low and notifies the admin if necessary
    private void checkStockAndNotify(Book book) {
        if (book.getStockQuantity() <= 5) { // Example threshold: 5
            // Send stock alert to the admin
            sendStockAlertToAdmin(book);
        }
    }

    // Sends a stock alert email to the admin
    private void sendStockAlertToAdmin(Book book) {
        String subject = "Stock Alert: " + book.getTitle();
        String message = "Attention: Stock for '" + book.getTitle() + "' is low. Current Stock: " + book.getStockQuantity();

        emailService.sendSimpleMessage(adminEmail, subject, message);
    }
}




