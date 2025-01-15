package com.reward.controller;


import com.reward.Service.CustomerService;
import com.reward.dto.CustomerDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class CustomerControllerTest {

    @InjectMocks
    private CustomerController customerController;

    @Mock
    private CustomerService customerService;

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

        when(customerService.registerCustomer(any(CustomerDTO.class))).thenReturn("John Doe");

        ResponseEntity<String> response = customerController.registerCustomer(customerDTO);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals("John DoeCustomer Register successfully.", response.getBody());
    }

    @Test
    public void testLoginCustomer() {
        CustomerDTO customerDTO = new CustomerDTO();
        customerDTO.setEmail("john.doe@example.com");
        customerDTO.setPassword("password");

        when(customerService.loginCustomer(any(CustomerDTO.class))).thenReturn("john.doe@example.com");

        ResponseEntity<String> response = customerController.loginCustomer(customerDTO);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Customer john.doe@example.com was successfully LogiIn.", response.getBody());
    }

    @Test
    public void testLogoffCustomer() {
        String token = "some-token";

        when(customerService.logoffCustomer(token)).thenReturn(token);

        ResponseEntity<String> response = customerController.logoffCustomer(token);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Customer some-token was successfully Logout.", response.getBody());
    }
}
