package com.project.springbookhub.payload.response;

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
public class OrderResponse {

    private Long id;

    private Client client;

    private LocalDateTime orderDate;
}
