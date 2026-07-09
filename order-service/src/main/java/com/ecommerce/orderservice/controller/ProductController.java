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

    @GetMapping("/{id}")
    public ProductResponseDTO getProduct(@PathVariable Long id) {
        // Giả lập lấy dữ liệu Entity từ DB
        ProductEntity entity = new ProductEntity(
                id,
                "Sản phẩm mẫu",
                "SKU-123",
                100.0,   // importPrice (nhạy cảm)
                150.0,   // sellPrice
                50       // stockQuantity (nhạy cảm)
        );

        // Map Entity sang DTO để chỉ trả về các thông tin cần thiết
        ProductResponseDTO responseDTO = new ProductResponseDTO(
                entity.getId(),
                entity.getName(),
                entity.getSellPrice()
        );

        return responseDTO;
    }
}
