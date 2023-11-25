package com.project.springbookhub.payload.mapper;

import com.project.springbookhub.model.concretes.Order;
import com.project.springbookhub.payload.request.OrderRequest;
import com.project.springbookhub.payload.response.OrderResponse;
import org.springframework.stereotype.Component;

@Component
public class OrderDto {

    public Order mapOrderRequestToOrder(OrderRequest orderRequest) {
        return Order.builder()
                .orderDate(orderRequest.getOrderDate())
                .client(orderRequest.getClient())
                .build();
    }

    public OrderResponse mapOrderToOrderResponse(Order order) {
        return OrderResponse.builder()
                .id(order.getId())
                .client(order.getClient())
                .orderDate(order.getOrderDate())
                .build();
    }

    public void updateOrderFromOrderRequest(OrderRequest orderRequest, Order order) {

        order.setOrderDate(orderRequest.getOrderDate());
        order.setClient(orderRequest.getClient());
    }
}
