package com.project.springbookhub.model.concretes;

import lombok.*;

import javax.persistence.*;
import java.math.BigDecimal;

/**
 * The Book entity represents a book in the BookHub application.
 * It includes details like title, author, price, and description.
 */

@Entity
@Table(name = "books")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String author;

    @Column(nullable = false)
    private BigDecimal price;

    @Column(length = 2000)
    private String description;

    @Column(nullable = false)
    private Integer stockQuantity;


}

