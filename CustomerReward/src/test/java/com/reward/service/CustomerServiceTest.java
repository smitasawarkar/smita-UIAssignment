package com.reward.Service;

import com.reward.dto.CustomerDTO;
import com.reward.entity.Customer;
import com.reward.repository.CustomerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

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
        customer.setName(customerDTO.getName());
        customer.setEmail(customerDTO.getEmail());
        customer.setPassword("encodedPassword");

        when(passwordEncoder.encode(customerDTO.getPassword())).thenReturn("encodedPassword");
        when(customerRepository.save(any(Customer.class))).thenReturn(customer);

        String result = customerService.registerCustomer(customerDTO);

        assertEquals(customer.getName(), result);
        verify(passwordEncoder, times(1)).encode(customerDTO.getPassword());
        verify(customerRepository, times(1)).save(any(Customer.class));
    }

    @Test
    public void testLoginCustomer() {
        CustomerDTO customerDTO = new CustomerDTO();
        customerDTO.setEmail("john.doe@example.com");
        customerDTO.setPassword("password");

        Customer customer = new Customer();
        customer.setEmail(customerDTO.getEmail());
        customer.setPassword("encodedPassword");

        when(customerRepository.findByEmail(customerDTO.getEmail())).thenReturn(customer);
        when(passwordEncoder.matches(customerDTO.getPassword(), customer.getPassword())).thenReturn(true);

        String result = customerService.loginCustomer(customerDTO);

        assertEquals(customer.getEmail(), result);
        verify(customerRepository, times(1)).findByEmail(customerDTO.getEmail());
        verify(passwordEncoder, times(1)).matches(customerDTO.getPassword(), customer.getPassword());
    }

    @Test
    public void testLoginCustomerInvalidCredentials() {
        CustomerDTO customerDTO = new CustomerDTO();
        customerDTO.setEmail("john.doe@example.com");
        customerDTO.setPassword("wrongPassword");

        Customer customer = new Customer();
        customer.setEmail(customerDTO.getEmail());
        customer.setPassword("encodedPassword");

        when(customerRepository.findByEmail(customerDTO.getEmail())).thenReturn(customer);
        when(passwordEncoder.matches(customerDTO.getPassword(), customer.getPassword())).thenReturn(false);

        Exception exception = assertThrows(RuntimeException.class, () -> {
            customerService.loginCustomer(customerDTO);
        });

        assertEquals("Invalid credentials", exception.getMessage());
        verify(customerRepository, times(1)).findByEmail(customerDTO.getEmail());
        verify(passwordEncoder, times(1)).matches(customerDTO.getPassword(), customer.getPassword());
    }

    @Test
    public void testGetCustomerById() {
        Long customerId = 1L;
        Customer customer = new Customer();
        customer.setId(customerId);
        customer.setName("John Doe");
        customer.setEmail("john.doe@example.com");
        customer.setPassword("encodedPassword");

        when(customerRepository.findById(customerId)).thenReturn(Optional.of(customer));

        CustomerDTO result = customerService.getCustomerById(customerId);

        assertEquals(customer.getId(), result.getId());
        assertEquals(customer.getName(), result.getName());
        assertEquals(customer.getEmail(), result.getEmail());
        assertEquals(customer.getPassword(), result.getPassword());
        verify(customerRepository, times(1)).findById(customerId);
    }

    @Test
    public void testGetCustomerByIdNotFound() {
        Long customerId = 1L;

        when(customerRepository.findById(customerId)).thenReturn(Optional.empty());

        Exception exception = assertThrows(RuntimeException.class, () -> {
            customerService.getCustomerById(customerId);
        });

        assertEquals("Customer not found with ID " + customerId, exception.getMessage());
        verify(customerRepository, times(1)).findById(customerId);
    }

    @Test
    public void testLogoffCustomer() {
        String token = "token123";

        String result = customerService.logoffCustomer(token);

        assertEquals(token, result);
    }
}