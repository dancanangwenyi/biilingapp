package com.example.billingapp.service.impl;

import com.example.billingapp.dto.request.CreateCustomerRequestDTO;
import com.example.billingapp.dto.request.UpdateCustomerRequestDTO;
import com.example.billingapp.dto.response.CustomerResponseDTO;
import com.example.billingapp.exception.ConflictException;
import com.example.billingapp.mapper.CustomerMapper;
import com.example.billingapp.model.Customer;
import com.example.billingapp.repository.CustomerRepository;
import com.example.billingapp.service.CustomerService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;
    private final CustomerMapper customerMapper;

    @Override
    public Page<CustomerResponseDTO> getCustomers(Pageable pageable) {
        Page<Customer> customers = customerRepository.findAll(pageable);
        return customers.map(customerMapper::toResponseDTO);
    }

    @Transactional
    @Override
    public CustomerResponseDTO createCustomer(CreateCustomerRequestDTO request) {
        Optional<Customer> existingCustomer = customerRepository.findCustomerByEmail(request.email());
        if (existingCustomer.isPresent()) {
            throw new ConflictException("Customer with email " + request.email() + " already exists");
        }
        Customer customer = new Customer();
        customer.setName(request.name());
        customer.setEmail(request.email());
        customer.setPhone(request.phone());
        customerRepository.save(customer);
        return null;
    }

    @Transactional
    @Override
    public CustomerResponseDTO updateCustomerById(Long id, UpdateCustomerRequestDTO request) {
        Optional<Customer> customer = customerRepository.findById(id);
        if(customer.isEmpty()){
            throw new EntityNotFoundException("Customer not found with ID: " + id);
        }
        Customer customerToUpdate = customer.get();
        if(request.name() != null) {
            customerToUpdate.setName(request.name());
        }
        if(request.email() != null && !request.email().equals(customerToUpdate.getEmail())) {
            Optional<Customer> existingCustomer = customerRepository.findCustomerByEmail(request.email());
            if (existingCustomer.isPresent()) {
                throw new ConflictException("Customer with email " + request.email() + " already exists");
            }
            customerToUpdate.setEmail(request.email());
        }
        if(request.phone() != null) {
            customerToUpdate.setPhone(request.phone());
        }
        return customerMapper.toResponseDTO(customerRepository.save(customerToUpdate));

    }

    @Transactional(readOnly = true)
    @Override
    public CustomerResponseDTO getCustomerById(Long id) {
        Optional<Customer> customer = customerRepository.findById(id);
        if(customer.isEmpty()){
            throw new EntityNotFoundException("Customer not found with ID: " + id);
        }
        return customerMapper.toResponseDTO(customer.get());
    }

    @Transactional
    @Override
    public String deleteCustomerById(Long id) {
        Optional<Customer> customer = customerRepository.findById(id);
        if(customer.isEmpty()){
            throw new EntityNotFoundException("Customer not found with ID: " + id);
        }
        customerRepository.deleteById(id);
        return "Customer deleted successfully";
    }
}
