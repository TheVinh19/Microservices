package com.ecommerce.customerservice.service;

import com.ecommerce.customerservice.entity.Customer;
import com.ecommerce.customerservice.exception.ResourceNotFoundException;
import com.ecommerce.customerservice.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CustomerService {
    @Autowired
    private CustomerRepository customerRepository;

    public Customer getCustomerById(Long id) {
        return customerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Customer with ID " + id + " not found"));
    }
}
