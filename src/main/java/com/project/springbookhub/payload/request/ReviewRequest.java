package com.project.springbookhub.payload.request;

import com.project.springbookhub.model.concretes.Book;
import com.project.springbookhub.model.concretes.Client;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.PastOrPresent;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReviewRequest {

    @NotNull(message = "Book cannot be null")
    private Book book;

    @NotNull(message = "Client cannot be null")
    private Client client;

    @NotNull(message = "Content cannot be null")
    @Size(min = 10, max = 1000, message = "Content must be between 10 and 1000 characters")
    private String content;

    @NotNull(message = "Review date cannot be null")
    @PastOrPresent(message = "Review date cannot be in the future")
    private LocalDateTime reviewDate;
}
