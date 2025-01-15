package com.reward.dto;


import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public class TransactionDTO {
    private Long id;

    @NotNull(message = "Customer ID cannot be null")
    private Long customerId;

    @NotNull(message = "Amount cannot be null")
    @Min(value = 0, message = "Amount should be positive")
    private Double amount;

    @NotNull(message = "Date cannot be null")
    private LocalDate date;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public TransactionDTO() {
    }

    public TransactionDTO(Long id, Long customerId, Double amount, LocalDate date) {
        this.id = id;
        this.customerId = customerId;
        this.amount = amount;
        this.date = date;
    }


}
