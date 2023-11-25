package com.project.springbookhub.payload.response;

import com.project.springbookhub.model.concretes.Book;
import com.project.springbookhub.model.concretes.Client;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReviewResponse {

    private Long id;

    private Book book;

    private Client client;

    private String content;

    private LocalDateTime reviewDate;
}
