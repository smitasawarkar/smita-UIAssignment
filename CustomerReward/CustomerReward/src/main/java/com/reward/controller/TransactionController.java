package com.reward.controller;

import com.reward.Service.TransactionService;
import com.reward.dto.TransactionDTO;
import com.reward.entity.Transaction;
//import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/transactions")
public class TransactionController {
    @Autowired
    private TransactionService transactionService;

    @PostMapping
   // @Operation(summary = "Add the transaction", description = "return 201 creaded status code with json body")
    public ResponseEntity<Transaction> addTransaction(@Valid @RequestBody TransactionDTO transactionDTO) {
        Transaction transaction = new Transaction();
        transaction.setCustomerId(transactionDTO.getCustomerId());
        transaction.setAmount(transactionDTO.getAmount());
        transaction.setDate(transactionDTO.getDate());
        Transaction createdTransaction = transactionService.addTransaction(transaction);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdTransaction);
    }
    @GetMapping("/getAllTransactions")
   // @Operation(summary = "get All transactions on the customerId start date End Date", description = "return 200 OK status code with json body")
    public ResponseEntity<List<Transaction>> getTransactions(@RequestParam Long customerId, @RequestParam LocalDate startDate, @RequestParam LocalDate endDate) {
        return ResponseEntity.ok(transactionService.getTransactions(customerId, startDate, endDate));
    }

    @PutMapping("/update/{id}")
   // @Operation(summary = "Update the transactions on the Id basis ", description = "return 200 OK status code with updated json body")
    public ResponseEntity<Transaction> updateTransaction(@PathVariable Long id, @Valid @RequestBody TransactionDTO transactionDTO) {
        Transaction updatedTransaction = new Transaction();
        updatedTransaction.setAmount(transactionDTO.getAmount());
        updatedTransaction.setDate(transactionDTO.getDate());
        Transaction transaction = transactionService.updateTransaction(id, updatedTransaction);
        return ResponseEntity.status(HttpStatus.OK).body(transaction);
    }

    @DeleteMapping("/delete/{id}")
   // @Operation(summary = "delete the transactions By ID basis", description = "return 200 OK status code with success message")
    public ResponseEntity<String> deleteTransaction(@PathVariable Long id) {
        transactionService.deleteTransaction(id);
        return ResponseEntity.status(HttpStatus.OK).body("Transaction with ID " + id + " was successfully deleted.");
    }
}