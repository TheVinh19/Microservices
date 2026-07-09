package com.ecommerce.orderservice.controller;

import com.ecommerce.orderservice.dto.PagedResponse;
import com.ecommerce.orderservice.dto.UserDTO;
import com.ecommerce.orderservice.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping
    public ResponseEntity<PagedResponse<UserDTO>> getAllUsers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        PagedResponse<UserDTO> response = userService.getAllUsers(page, size);
        return ResponseEntity.ok(response);
    }
}
