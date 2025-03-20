package com.reward.Service;

import com.reward.dto.CustomerDTO;
import com.reward.entity.Customer;
import com.reward.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Service
public class CustomerService {
    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public String registerCustomer(CustomerDTO customerDTO) {
        Customer customer = new Customer();
        customer.setName(customerDTO.getName());
        customer.setEmail(customerDTO.getEmail());
        customer.setPassword(passwordEncoder.encode(customerDTO.getPassword()));
        customerRepository.save(customer);
        return customer.getName();
    }

    public String loginCustomer(CustomerDTO customerDTO) {
        Customer customer = customerRepository.findByEmail(customerDTO.getEmail());
        if (customer != null && passwordEncoder.matches(customerDTO.getPassword(), customer.getPassword())) {

            return customer.getEmail();
        }
        throw new RuntimeException("Invalid credentials");
    }

    public CustomerDTO getCustomerById(Long id) {
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Customer not found with ID " + id));
        return new CustomerDTO(customer.getId(), customer.getName(), customer.getEmail(), customer.getPassword());
    }

    public String logoffCustomer(String token) {
        // Invalidate the token (for simplicity, no actual token management)
        return token;
    }
}
