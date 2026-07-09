package com.ecommerce.orderservice.dto;

import java.time.LocalDateTime;

public class OrderDTO {
    
    private Long id;
    private Long customerId;
    private Long productId;
    private LocalDateTime orderDate;
    private Double totalAmount;

    public OrderDTO() {}

    public OrderDTO(Long id, Long customerId, Long productId, LocalDateTime orderDate, Double totalAmount) {
        this.id = id;
        this.customerId = customerId;
        this.productId = productId;
        this.orderDate = orderDate;
        this.totalAmount = totalAmount;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getCustomerId() { return customerId; }
    public void setCustomerId(Long customerId) { this.customerId = customerId; }
    public Long getProductId() { return productId; }
    public void setProductId(Long productId) { this.productId = productId; }
    public LocalDateTime getOrderDate() { return orderDate; }
    public void setOrderDate(LocalDateTime orderDate) { this.orderDate = orderDate; }
    public Double getTotalAmount() { return totalAmount; }
    public void setTotalAmount(Double totalAmount) { this.totalAmount = totalAmount; }
}
