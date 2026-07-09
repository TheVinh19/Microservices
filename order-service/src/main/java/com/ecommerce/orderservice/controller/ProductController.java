package com.ecommerce.orderservice.controller;

import com.ecommerce.orderservice.dto.ProductResponseDTO;
import com.ecommerce.orderservice.entity.ProductEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/products")
public class ProductController {
    private final com.ecommerce.orderservice.repository.ProductRepository productRepository;

    public ProductController(com.ecommerce.orderservice.repository.ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @GetMapping("/{id}")
    public ProductResponseDTO getProduct(@PathVariable Long id) {
        // Lấy dữ liệu Entity từ DB
        ProductEntity entity = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found with id: " + id));

        // Map Entity sang DTO để chỉ trả về các thông tin cần thiết
        ProductResponseDTO responseDTO = new ProductResponseDTO(
                entity.getId(),
                entity.getName(),
                entity.getSellPrice()
        );

        return responseDTO;
    }
}
