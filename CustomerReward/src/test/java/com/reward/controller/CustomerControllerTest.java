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
        String expectedResponse = "Success"; // Replace with the actual expected response

        when(customerService.registerCustomer(customerDTO)).thenReturn(expectedResponse);

        ResponseEntity<String> response = customerController.registerCustomer(customerDTO);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(expectedResponse + "Customer Register successfully.", response.getBody());
        verify(customerService, times(1)).registerCustomer(customerDTO);
    }
    @Test
    public void testLoginCustomer() {
        CustomerDTO customerDTO = new CustomerDTO();
        String token = "token123";
        when(customerService.loginCustomer(customerDTO)).thenReturn(token);

        ResponseEntity<String> response = customerController.loginCustomer(customerDTO);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Customer " + token + " was successfully LogIn.", response.getBody());
        verify(customerService, times(1)).loginCustomer(customerDTO);
    }

    @Test
    public void testGetCustomerById() {
        Long customerId = 1L;
        CustomerDTO customerDTO = new CustomerDTO();
        when(customerService.getCustomerById(customerId)).thenReturn(customerDTO);

        ResponseEntity<CustomerDTO> response = customerController.getCustomerById(customerId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(customerDTO, response.getBody());
        verify(customerService, times(1)).getCustomerById(customerId);
    }

    @Test
    public void testLogoffCustomer() {
        String token = "token123";
        String expectedResponse = "Logout successful.";
        when(customerService.logoffCustomer(token)).thenReturn(expectedResponse);

        ResponseEntity<String> response = customerController.logoffCustomer(token);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Customer " + expectedResponse + " was successfully Logout.", response.getBody());
        verify(customerService, times(1)).logoffCustomer(token);
    }
}