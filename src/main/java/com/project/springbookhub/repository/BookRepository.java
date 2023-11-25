package com.project.springbookhub.repository;

import com.project.springbookhub.model.concretes.Book;
import com.project.springbookhub.model.concretes.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BookRepository extends JpaRepository<Book, Long> {
    List<Book> findByAuthor(String author);
    List<Book> findByTitle(String title);
    List<Book> findByAuthorAndTitle(String author, String title);


}
