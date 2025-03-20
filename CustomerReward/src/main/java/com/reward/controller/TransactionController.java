package com.reward.controller;

import com.reward.Service.TransactionService;
import com.reward.dto.TransactionDTO;
import com.reward.entity.Transaction;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
//import io.swagger.v3.oas.annotations.Operation;
//import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/transactions")
@Tag(name = "transactionController",description = "To Perform Transaction operation")
public class TransactionController {
    @Autowired
    private TransactionService transactionService;

    @Operation(summary = "Create a new transaction", description = "Add a new transaction to the system")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Successfully add the transaction"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping
    public ResponseEntity<Transaction> addTransaction(@Parameter(description = "insert transaction details", required = true)
                                                      @Valid @RequestBody TransactionDTO transactionDTO) {
       System.out.println("Received request to add transaction: " + transactionDTO);
       Transaction transaction = transactionService.addTransaction(transactionDTO);
       return ResponseEntity.status(HttpStatus.CREATED).body(transaction);
   }

    @Operation(summary = "Get transaction by customerId startDate and endDate", description = "Fetch a single transaction ")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully fetched the transaction"),
            @ApiResponse(responseCode = "404", description = "transaction not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
   @GetMapping("/getAllTransactions")
   public ResponseEntity<List<Transaction>> getTransactions(@Parameter(description = "ID of the transaction to be fetched", example = "1")
                                                            @RequestParam Long customerId,
                                                            @Parameter(description = "start Date of the transaction to be fetch", example = "2025-03-01")
                                                            @RequestParam LocalDate startDate,
                                                            @Parameter(description = "End Date of the transaction to be fetch", example = "2025-03-01")
                                                            @RequestParam LocalDate endDate) {
        System.out.println("customerId :: "+customerId);
        return ResponseEntity.ok(transactionService.getTransactions(customerId, startDate, endDate));
    }

    @Operation(summary = "Update an existing transaction", description = "Update a transaction's details")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully updated the transaction"),
            @ApiResponse(responseCode = "404", description = "transaction not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PutMapping("/update/{id}")
    public ResponseEntity<Transaction> updateTransaction(@Parameter(description = "ID of the transaction to be updated", example = "1")
                                                         @PathVariable Long id,
                                                         @Parameter(description = "Updated transaction details", required = true)
                                                         @Valid @RequestBody TransactionDTO transactionDTO) {
        Transaction updatedTransaction = new Transaction();
        updatedTransaction.setAmount(transactionDTO.getAmount());
        updatedTransaction.setDate(transactionDTO.getDate());
        Transaction transaction = transactionService.updateTransaction(id, updatedTransaction);
        return ResponseEntity.status(HttpStatus.OK).body(transaction);
    }

    @Operation(summary = "Delete a transaction", description = "Remove a transaction from the system")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully deleted the transaction"),
            @ApiResponse(responseCode = "404", description = "transaction not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @DeleteMapping("/delete/{id}")
   public ResponseEntity<String> deleteTransaction( @Parameter(description = "ID of the transaction to be deleted", example = "1")
                                                    @PathVariable Long id) {
        transactionService.deleteTransaction(id);
        return ResponseEntity.status(HttpStatus.OK).body("Transaction with ID " + id + " was successfully deleted.");
    }
}