package com.reward.controller;

import com.reward.Service.CustomerService;
import com.reward.dto.CustomerDTO;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/customers")
public class CustomerController {
    @Autowired
    private CustomerService customerService;

    @PostMapping("/register")
    public ResponseEntity<String> registerCustomer(@Valid @RequestBody CustomerDTO customerDTO) {
        System.out.println("Received CustomerDTO: " + customerDTO);
     String cusRegs= customerService.registerCustomer(customerDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(cusRegs+ "Customer Register successfully.");
    }

    @PostMapping("/login")
    public ResponseEntity<String> loginCustomer(@Valid @RequestBody CustomerDTO customerDTO) {
        String token = customerService.loginCustomer(customerDTO);
        return ResponseEntity.status(HttpStatus.OK).body("Customer " + token + " was successfully LogiIn.");
    }

    @PostMapping("/logoff")
    public ResponseEntity<String> logoffCustomer(@RequestBody String token) {
       String logoff = customerService.logoffCustomer(token);
        return ResponseEntity.status(HttpStatus.OK).body("Customer " + logoff + " was successfully Logout.");
    }
}