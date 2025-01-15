package com.reward.Service;

import com.reward.dto.CustomerDTO;
import com.reward.entity.Customer;
import com.reward.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

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

    public String logoffCustomer(String token) {
        // Invalidate the token (for simplicity, no actual token management)
        return token;
    }
}
