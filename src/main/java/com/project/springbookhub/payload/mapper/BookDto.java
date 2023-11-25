package com.project.springbookhub.payload.mapper;

import com.project.springbookhub.model.concretes.Book;

import com.project.springbookhub.payload.request.BookRequest;
import com.project.springbookhub.payload.response.BookResponse;
import org.springframework.stereotype.Component;


@Component
public class BookDto {


    public Book mapBookRequestToBook(BookRequest bookRequest) {
        return Book.builder()
                .title(bookRequest.getTitle())
                .author(bookRequest.getAuthor())
                .price(bookRequest.getPrice())
                .description(bookRequest.getDescription())
                .stockQuantity(bookRequest.getStockQuantity())
                .build();
    }

    public BookResponse mapBookToBookResponse(Book book) {
        return BookResponse.builder()
                .id(book.getId())
                .author(book.getAuthor())
                .title(book.getTitle())
                .price(book.getPrice())
                .description(book.getDescription())
                .stockQuantity(book.getStockQuantity())
                .build();
    }
    public static void updateBookFromBookRequest(BookRequest bookRequest, Book book) {
        book.setAuthor(bookRequest.getAuthor());
        book.setTitle(bookRequest.getTitle());
        book.setPrice(bookRequest.getPrice());
        book.setDescription(bookRequest.getDescription());
        book.setStockQuantity(bookRequest.getStockQuantity());


    }
}
