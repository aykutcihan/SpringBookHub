package com.project.springbookhub.service;

import com.project.springbookhub.exception.OrderNotFoundException;
import com.project.springbookhub.model.concretes.Client;
import com.project.springbookhub.model.concretes.Order;
import com.project.springbookhub.payload.ResponseMessage;
import com.project.springbookhub.payload.request.OrderRequest;
import com.project.springbookhub.payload.response.OrderResponse;
import com.project.springbookhub.repository.OrderRepository;
import com.project.springbookhub.payload.mapper.OrderDto;
import com.project.springbookhub.util.Messages;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class OrderServiceTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private OrderDto orderDto;

    @InjectMocks
    private OrderService orderService;

    private OrderRequest orderRequest;
    private Order order;
    private OrderResponse orderResponse;

    @BeforeEach
    void setUp() {
        // Eksiksiz bir Client nesnesi oluşturun
        Client client = Client.builder()
                .id(1L)
                .username("testClient")
                .email("test@example.com")
                .name("Test")
                .surname("User")
                .password("TestPassword123")
                .phoneNumber("123-456-7890")
                .build();

        orderRequest = new OrderRequest();
        orderRequest.setClient(client);
        orderRequest.setOrderDate(LocalDateTime.now());

        order = new Order();
        order.setId(1L);
        order.setClient(client);
        order.setOrderDate(LocalDateTime.now());

        orderResponse = new OrderResponse();
        orderResponse.setId(order.getId());
        orderResponse.setClient(client);
        orderResponse.setOrderDate(order.getOrderDate());
    }

    @Test
    void createOrderTest() {


        when(orderDto.mapOrderRequestToOrder(any(OrderRequest.class))).thenReturn(order);
        when(orderDto.mapOrderToOrderResponse(any(Order.class))).thenReturn(orderResponse);

        // Arrange
        when(orderRepository.save(any(Order.class))).thenReturn(order);

        // Act
        ResponseMessage<OrderResponse> response = orderService.createOrder(orderRequest);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getHttpStatus());
        assertEquals("Order Created Successfully", response.getMessage());
        assertEquals(orderResponse, response.getObject());

        // Verify
        verify(orderRepository).save(any(Order.class));
        verify(orderDto).mapOrderRequestToOrder(any(OrderRequest.class));
        verify(orderDto).mapOrderToOrderResponse(any(Order.class));
    }

    @Test
    void getOrderByIdTest_Successful() {
        // Arrange
        Long existingOrderId = 1L;
        when(orderRepository.findById(existingOrderId)).thenReturn(Optional.of(order));
        when(orderDto.mapOrderToOrderResponse(any(Order.class))).thenReturn(orderResponse);

        // Act
        ResponseMessage<OrderResponse> response = orderService.getOrderById(existingOrderId);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.CREATED, response.getHttpStatus());
        assertEquals("Order found", response.getMessage());
        assertEquals(orderResponse, response.getObject());

        // Verify
        verify(orderRepository).findById(existingOrderId);
        verify(orderDto).mapOrderToOrderResponse(order);
    }

    @Test
    void getOrderByIdTest_NotFound() {
        // Arrange
        Long nonExistingOrderId = 99L;
        when(orderRepository.findById(nonExistingOrderId)).thenReturn(Optional.empty());

        // Act & Assert
        OrderNotFoundException thrown = assertThrows(OrderNotFoundException.class,
                () -> orderService.getOrderById(nonExistingOrderId));

        // Verify
        verify(orderRepository).findById(nonExistingOrderId);
        assertEquals(String.format(Messages.ORDER_NOT_FOUND, nonExistingOrderId), thrown.getMessage());
    }

    @Test
    void getAllOrdersTest() {
        // Arrange
        List<Order> orders = Collections.singletonList(order);
        when(orderRepository.findAll()).thenReturn(orders);
        when(orderDto.mapOrderToOrderResponse(any(Order.class))).thenReturn(orderResponse);

        // Act
        List<OrderResponse> responses = orderService.getAllOrders();

        // Assert
        assertNotNull(responses);
        assertFalse(responses.isEmpty());
        assertEquals(1, responses.size());
        assertEquals(orderResponse, responses.get(0));

        // Verify
        verify(orderRepository).findAll();
        verify(orderDto, times(orders.size())).mapOrderToOrderResponse(any(Order.class));
    }
    @Test
    void updateOrderTest_ExistingOrder() {
        // Arrange
        Long existingOrderId = 1L;
        when(orderRepository.findById(existingOrderId)).thenReturn(Optional.of(order));
        when(orderRepository.save(any(Order.class))).thenReturn(order);
        when(orderDto.mapOrderToOrderResponse(any(Order.class))).thenReturn(orderResponse);

        // Act
        ResponseMessage<OrderResponse> response = orderService.updateOrder(existingOrderId, orderRequest);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getHttpStatus());
        assertEquals("Order updated successfully", response.getMessage());
        assertEquals(orderResponse, response.getObject());

        // Verify
        verify(orderRepository).findById(existingOrderId);
        verify(orderDto).updateOrderFromOrderRequest(orderRequest, order);
        verify(orderRepository).save(order);
        verify(orderDto).mapOrderToOrderResponse(order);
    }

    @Test
    void updateOrderTest_NonExistingOrder() {
        // Arrange
        Long nonExistingOrderId = 99L;
        when(orderRepository.findById(nonExistingOrderId)).thenReturn(Optional.empty());

        // Act & Assert
        OrderNotFoundException thrown = assertThrows(OrderNotFoundException.class,
                () -> orderService.updateOrder(nonExistingOrderId, orderRequest));

        // Verify
        verify(orderRepository).findById(nonExistingOrderId);
        assertEquals(String.format(Messages.ORDER_NOT_FOUND, nonExistingOrderId), thrown.getMessage());
    }
    @Test
    void deleteOrderTest_ExistingOrder() {
        // Arrange
        Long existingOrderId = 1L;
        when(orderRepository.findById(existingOrderId)).thenReturn(Optional.of(order));

        // Act
        ResponseMessage<?> response = orderService.deleteOrder(existingOrderId);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getHttpStatus());
        assertEquals("Order deleted successfully", response.getMessage());

        // Verify
        verify(orderRepository).findById(existingOrderId);
        verify(orderRepository).deleteById(existingOrderId);
    }

    @Test
    void deleteOrderTest_NonExistingOrder() {
        // Arrange
        Long nonExistingOrderId = 99L;
        when(orderRepository.findById(nonExistingOrderId)).thenReturn(Optional.empty());

        // Act & Assert
        OrderNotFoundException thrown = assertThrows(OrderNotFoundException.class,
                () -> orderService.deleteOrder(nonExistingOrderId));

        // Verify
        verify(orderRepository).findById(nonExistingOrderId);
        assertEquals(String.format(Messages.ORDER_NOT_FOUND, nonExistingOrderId), thrown.getMessage());
    }
}
