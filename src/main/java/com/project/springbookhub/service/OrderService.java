package com.project.springbookhub.service;

import com.project.springbookhub.exception.BookNotFoundException;
import com.project.springbookhub.exception.OrderNotFoundException;
import com.project.springbookhub.model.concretes.Book;
import com.project.springbookhub.model.concretes.Client;
import com.project.springbookhub.model.concretes.Order;
import com.project.springbookhub.payload.ResponseMessage;
import com.project.springbookhub.payload.mapper.BookDto;
import com.project.springbookhub.payload.mapper.ClientDto;
import com.project.springbookhub.payload.mapper.OrderDto;
import com.project.springbookhub.payload.request.BookRequest;
import com.project.springbookhub.payload.request.OrderRequest;
import com.project.springbookhub.payload.response.BookResponse;
import com.project.springbookhub.payload.response.ClientResponse;
import com.project.springbookhub.payload.response.OrderResponse;
import com.project.springbookhub.repository.OrderRepository;
import com.project.springbookhub.util.Messages;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final OrderDto orderDto;

    public ResponseMessage<OrderResponse> createOrder(OrderRequest orderRequest) {

        Order order = orderRepository.save(orderDto.mapOrderRequestToOrder(orderRequest));
        OrderResponse orderResponse = orderDto.mapOrderToOrderResponse(order);

        return ResponseMessage.<OrderResponse>builder()
                .object(orderResponse)
                .message("Order Created Successfully")
                .httpStatus(HttpStatus.OK)
                .build();

    }

    public ResponseMessage<OrderResponse> getOrderById(Long id) {
        Order order = orderRepository.findById(id)
                    .orElseThrow(() -> new OrderNotFoundException(String.format(Messages.ORDER_NOT_FOUND,id)));

        OrderResponse orderResponse = orderDto.mapOrderToOrderResponse(order);

            return ResponseMessage.<OrderResponse>builder()
                    .object(orderResponse)
                    .message("Order found")
                    .httpStatus(HttpStatus.CREATED)
                    .build();
        }

    public List<OrderResponse> getAllOrders() {

            List<Order> orders = orderRepository.findAll();
            return orders.stream()
                    .map(orderDto::mapOrderToOrderResponse)
                    .collect(Collectors.toList());
        }

    public ResponseMessage<OrderResponse> updateOrder(Long id, OrderRequest orderRequest) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new OrderNotFoundException(String.format(Messages.ORDER_NOT_FOUND,id)));

        orderDto.updateOrderFromOrderRequest(orderRequest,order);
        Order updatedOrder = orderRepository.save(order);

        return ResponseMessage.<OrderResponse>builder()
                .object(orderDto.mapOrderToOrderResponse(updatedOrder))
                .message("Order updated successfully")
                .httpStatus(HttpStatus.OK)
                .build();

    }

    public ResponseMessage<?> deleteOrder(Long id) {

        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new OrderNotFoundException(String.format(Messages.ORDER_NOT_FOUND,id)));

        orderRepository.deleteById(order.getId());

        return ResponseMessage.<OrderResponse>builder()
                .message("Order deleted successfully")
                .httpStatus(HttpStatus.OK)
                .build();
    }

}
