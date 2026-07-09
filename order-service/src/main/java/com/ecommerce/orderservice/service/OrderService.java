package com.ecommerce.orderservice.service;

import com.ecommerce.orderservice.dto.ExternalCustomerDTO;
import com.ecommerce.orderservice.dto.ExternalProductDTO;
import com.ecommerce.orderservice.dto.OrderDTO;
import com.ecommerce.orderservice.dto.OrderRequestDTO;
import com.ecommerce.orderservice.entity.Order;
import com.ecommerce.orderservice.exception.ResourceNotFoundException;
import com.ecommerce.orderservice.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.client.HttpClientErrorException;

import java.time.LocalDateTime;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private RestTemplate restTemplate;

    public Order getOrderById(Long id) {
        return orderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Order with ID " + id + " not found"));
    }

    public OrderDTO createOrder(OrderRequestDTO request) {
        // 1. Call customer-service
        try {
            restTemplate.getForObject("http://localhost:8081/api/v1/customers/" + request.getCustomerId(), ExternalCustomerDTO.class);
        } catch (HttpClientErrorException.NotFound e) {
            throw new ResourceNotFoundException("Customer with ID " + request.getCustomerId() + " not found!");
        }

        // 2. Call product-service
        ExternalProductDTO product;
        try {
            product = restTemplate.getForObject("http://localhost:8082/api/v1/products/" + request.getProductId(), ExternalProductDTO.class);
        } catch (HttpClientErrorException.NotFound e) {
            throw new ResourceNotFoundException("Product with ID " + request.getProductId() + " not found!");
        }

        // 3. Calculate total
        Double totalAmount = product.getPrice() * request.getQuantity();

        // 4. Save Order
        Order order = new Order();
        order.setCustomerId(request.getCustomerId());
        order.setProductId(request.getProductId());
        order.setOrderDate(LocalDateTime.now());
        order.setTotalAmount(totalAmount);
        
        Order savedOrder;
        try {
            savedOrder = orderRepository.save(order);
        } catch (Exception e) {
            throw new RuntimeException("Database error: Could not save order", e);
        }

        return new OrderDTO(
                savedOrder.getId(),
                savedOrder.getCustomerId(),
                savedOrder.getProductId(),
                savedOrder.getOrderDate(),
                savedOrder.getTotalAmount()
        );
    }
}
