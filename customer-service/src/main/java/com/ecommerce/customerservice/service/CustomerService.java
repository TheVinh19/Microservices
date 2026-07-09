package com.ecommerce.customerservice.service;

import com.ecommerce.customerservice.dto.CustomerRequestDTO;
import com.ecommerce.customerservice.dto.CustomerResponseDTO;
import com.ecommerce.customerservice.entity.Customer;
import com.ecommerce.customerservice.exception.ResourceNotFoundException;
import com.ecommerce.customerservice.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Base64;
import java.util.Optional;

@Service
public class CustomerService {
    @Autowired
    private CustomerRepository customerRepository;

    public CustomerResponseDTO register(CustomerRequestDTO dto) {
        Customer customer = new Customer();
        customer.setFullName(dto.getFullName());
        customer.setEmail(dto.getEmail());
        // Simple encoding for demonstration purposes
        String encodedPassword = Base64.getEncoder().encodeToString(dto.getPassword().getBytes());
        customer.setPassword(encodedPassword);
        
        Customer savedCustomer = customerRepository.save(customer);
        return new CustomerResponseDTO(savedCustomer.getId(), savedCustomer.getFullName(), savedCustomer.getEmail());
    }

    public CustomerResponseDTO getCustomerById(Long id) {
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Khách hàng với ID " + id + " không tồn tại!"));
        return new CustomerResponseDTO(customer.getId(), customer.getFullName(), customer.getEmail());
    }

    public CustomerResponseDTO login(String email, String password) {
        Optional<Customer> optCustomer = customerRepository.findByEmail(email);
        if (optCustomer.isPresent()) {
            Customer customer = optCustomer.get();
            String encodedInputPassword = Base64.getEncoder().encodeToString(password.getBytes());
            if (customer.getPassword().equals(encodedInputPassword)) {
                return new CustomerResponseDTO(customer.getId(), customer.getFullName(), customer.getEmail());
            }
        }
        return null; // Login failed
    }
}
