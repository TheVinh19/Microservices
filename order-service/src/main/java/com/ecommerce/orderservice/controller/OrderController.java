package com.ecommerce.orderservice.controller;

import com.ecommerce.orderservice.dto.BaseResponse;
import com.ecommerce.orderservice.dto.OrderDTO;
import com.ecommerce.orderservice.dto.OrderRequestDTO;
import com.ecommerce.orderservice.entity.Order;
import com.ecommerce.orderservice.service.OrderService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/orders")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @GetMapping("/health-check")
    public String healthCheck() {
        return "Order Service is Up";
    }

    @GetMapping("/{id}")
    public ResponseEntity<Order> getOrderById(@PathVariable Long id) {
        Order order = orderService.getOrderById(id);
        return ResponseEntity.ok(order);
    }

    @PostMapping
    public ResponseEntity<BaseResponse<OrderDTO>> createOrder(@Valid @RequestBody OrderRequestDTO request) {
        OrderDTO createdOrder = orderService.createOrder(request);
        BaseResponse<OrderDTO> response = new BaseResponse<>(
                HttpStatus.CREATED.value(),
                "Tạo đơn hàng thành công",
                createdOrder
        );
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
}
