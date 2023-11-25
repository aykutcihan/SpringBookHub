package com.project.springbookhub.controller;

import com.project.springbookhub.payload.ResponseMessage;
import com.project.springbookhub.payload.request.OrderRequest;
import com.project.springbookhub.payload.response.BookResponse;
import com.project.springbookhub.payload.response.OrderResponse;
import com.project.springbookhub.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping("/create")
    @PreAuthorize("hasAnyAuthority('ROLE_CLIENT','ROLE_ADMIN')")
    public ResponseMessage<OrderResponse> createOrder (@RequestBody @Valid OrderRequest orderRequest) {
        return orderService.createOrder(orderRequest);
    }

    @GetMapping("/getOrderById/{id}")
    @PreAuthorize("hasAnyAuthority('ROLE_CLIENT','ROLE_ADMIN')")
    public ResponseMessage<OrderResponse> getOrderById(@PathVariable Long id) {
        return orderService.getOrderById(id);
    }

    @GetMapping("/getAllOrder")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public List<OrderResponse> getAllBooks() {
        return orderService.getAllOrders();
    }

    @PutMapping("/update/{id}")
    @PreAuthorize("hasAnyAuthority('ROLE_CLIENT','ROLE_ADMIN')")
    public ResponseMessage<OrderResponse> updateOrder(@PathVariable Long id,
                                                    @RequestBody @Valid OrderRequest orderRequest) {
        return orderService.updateOrder(id, orderRequest);
    }

    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasAnyAuthority('ROLE_CLIENT','ROLE_ADMIN')")
    public ResponseMessage<?> deleteOrder (@PathVariable Long id) {
        return orderService.deleteOrder(id);
    }

}
