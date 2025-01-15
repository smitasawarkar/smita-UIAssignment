package com.reward.service;


import com.reward.Service.CustomerService;
import com.reward.dto.CustomerDTO;
import com.reward.entity.Customer;
import com.reward.repository.CustomerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class CustomerServiceTest {

    @InjectMocks
    private CustomerService customerService;

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testRegisterCustomer() {
        CustomerDTO customerDTO = new CustomerDTO();
        customerDTO.setName("John Doe");
        customerDTO.setEmail("john.doe@example.com");
        customerDTO.setPassword("password");

        Customer customer = new Customer();
        customer.setName("John Doe");
        customer.setEmail("john.doe@example.com");
        customer.setPassword("encoded-password");

        when(passwordEncoder.encode(customerDTO.getPassword())).thenReturn("encoded-password");
        when(customerRepository.save(any(Customer.class))).thenReturn(customer);

        String result = customerService.registerCustomer(customerDTO);

        assertEquals("John Doe", result);
    }

    @Test
    public void testLoginCustomer() {
        CustomerDTO customerDTO = new CustomerDTO();
        customerDTO.setEmail("john.doe@example.com");
        customerDTO.setPassword("password");

        Customer customer = new Customer();
        customer.setEmail("john.doe@example.com");
        customer.setPassword("encoded-password");

        when(customerRepository.findByEmail(customerDTO.getEmail())).thenReturn(customer);
        when(passwordEncoder.matches(customerDTO.getPassword(), customer.getPassword())).thenReturn(true);

        String result = customerService.loginCustomer(customerDTO);

        assertEquals("john.doe@example.com", result);
    }

    @Test
    public void testLoginCustomerInvalidCredentials() {
        CustomerDTO customerDTO = new CustomerDTO();
        customerDTO.setEmail("john.doe@example.com");
        customerDTO.setPassword("wrong-password");

        Customer customer = new Customer();
        customer.setEmail("john.doe@example.com");
        customer.setPassword("encoded-password");

        when(customerRepository.findByEmail(customerDTO.getEmail())).thenReturn(customer);
        when(passwordEncoder.matches(customerDTO.getPassword(), customer.getPassword())).thenReturn(false);

        assertThrows(RuntimeException.class, () -> customerService.loginCustomer(customerDTO));
    }

    @Test
    public void testLogoffCustomer() {
        String token = "some-token";

        String result = customerService.logoffCustomer(token);

        assertEquals("some-token", result);
    }
}