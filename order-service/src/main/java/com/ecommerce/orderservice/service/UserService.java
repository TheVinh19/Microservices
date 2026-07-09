package com.ecommerce.orderservice.service;

import com.ecommerce.orderservice.dto.PagedResponse;
import com.ecommerce.orderservice.dto.UserDTO;
import com.ecommerce.orderservice.entity.UserEntity;
import com.ecommerce.orderservice.exception.ResourceNotFoundException;
import com.ecommerce.orderservice.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public UserDTO getUserById(Long id) {
        UserEntity user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User với ID " + id + " không tồn tại!"));
        return new UserDTO(user.getId(), user.getName(), user.getEmail());
    }

    public PagedResponse<UserDTO> getAllUsers(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<UserEntity> userPage = userRepository.findAll(pageable);
        
        List<UserDTO> userDTOs = userPage.getContent().stream()
                .map(user -> new UserDTO(user.getId(), user.getName(), user.getEmail()))
                .collect(Collectors.toList());

        return new PagedResponse<>(userDTOs, userPage.getTotalElements(), userPage.getNumber(), userPage.getSize());
    }
}
