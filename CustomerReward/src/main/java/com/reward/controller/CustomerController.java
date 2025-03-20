package com.reward.controller;

import com.reward.Service.CustomerService;
import com.reward.dto.CustomerDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/customers")
@Tag(name = "CustomerController",description = " to Register login logout customer")
public class CustomerController {
    @Autowired
    private CustomerService customerService;

    @Operation(summary = "Register a new customer", description = "Add a new customer details in system")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Successfully add the customer"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping("/register")
    public ResponseEntity<String> registerCustomer(@Parameter(description = "insert transaction details", required = true)
                                                   @Valid @RequestBody CustomerDTO customerDTO) {
        System.out.println("Received CustomerDTO: " + customerDTO);
     String cusRegs= customerService.registerCustomer(customerDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(cusRegs+ "Customer Register successfully.");
    }

    @Operation(summary = "Customer login", description = "Add a new customer login details in system")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Successfully add the login details"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping("/login")
    public ResponseEntity<String> loginCustomer(@Parameter(description = "insert customer login details", required = true)
                                                @Valid @RequestBody CustomerDTO customerDTO) {
        String token = customerService.loginCustomer(customerDTO);
        return ResponseEntity.status(HttpStatus.OK).body("Customer " + token + " was successfully LogIn.");
    }
    @Operation(summary = "Get customer by Id ", description = "Fetch a single customer ")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully fetched the customer"),
            @ApiResponse(responseCode = "404", description = "customer not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/getCustomerById/{id}")
    public ResponseEntity<CustomerDTO> getCustomerById(@Parameter(description = "ID of the customer to be fetch", example = "1")
                                                       @PathVariable Long id) {
        CustomerDTO customer = customerService.getCustomerById(id);
        return ResponseEntity.status(HttpStatus.OK).body(customer);
    }
    @Operation(summary = "insert logout details", description = "Add a new customer Logout details in system")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully logout Customer"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping("/logoff")
    public ResponseEntity<String> logoffCustomer(@Parameter(description = "insert Logout details", required = true)
                                                 @RequestBody String token) {
       String logoff = customerService.logoffCustomer(token);
        return ResponseEntity.status(HttpStatus.OK).body("Customer " + logoff + " was successfully Logout.");
    }
}