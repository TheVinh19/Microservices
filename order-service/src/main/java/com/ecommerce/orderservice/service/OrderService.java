package com.ecommerce.orderservice.service;

import com.ecommerce.orderservice.dto.OrderDTO;
import com.ecommerce.orderservice.dto.OrderRequestDTO;
import com.ecommerce.orderservice.entity.Order;
import com.ecommerce.orderservice.entity.ProductEntity;
import com.ecommerce.orderservice.exception.ResourceNotFoundException;
import com.ecommerce.orderservice.repository.OrderRepository;
import com.ecommerce.orderservice.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ProductRepository productRepository;

    public Order getOrderById(Long id) {
        return orderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Order với ID " + id + " không tồn tại trên hệ thống!"));
    }

    public OrderDTO createOrder(OrderRequestDTO request) {
        // 1. Kiểm tra khóa học tồn tại không (dùng ProductEntity thay cho Course)
        ProductEntity course = productRepository.findById(request.getCourseId())
                .orElseThrow(() -> new ResourceNotFoundException("Khóa học với ID " + request.getCourseId() + " không tồn tại!"));

        // 2. Tính tổng tiền (lấy từ sellPrice)
        Double totalAmount = course.getSellPrice();

        // 3. Lưu vào DB
        Order order = new Order();
        order.setStudentId(request.getStudentId());
        order.setCourseId(request.getCourseId());
        order.setTotalAmount(totalAmount);
        
        Order savedOrder = orderRepository.save(order);

        // 4. Trả về OrderDTO
        return new OrderDTO(
                savedOrder.getId(),
                savedOrder.getStudentId(),
                savedOrder.getCourseId(),
                savedOrder.getTotalAmount()
        );
    }
}
