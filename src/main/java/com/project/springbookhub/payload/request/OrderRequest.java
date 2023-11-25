package com.project.springbookhub.payload.request;

import com.project.springbookhub.model.concretes.Client;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderRequest {

    private Client client;

    private LocalDateTime orderDate;
}
