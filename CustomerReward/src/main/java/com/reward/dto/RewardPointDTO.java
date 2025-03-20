package com.reward.dto;

import jakarta.validation.constraints.NotNull;

public class RewardPointDTO {
    private Long id;

    @NotNull(message = "Customer ID cannot be null")
    private Long customerId;

    @NotNull(message = "Month cannot be null")
    private Integer month;

    @NotNull(message = "Year cannot be null")
    private Integer year;

    @NotNull(message = "Points cannot be null")
    private Integer points;

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

    public Integer getMonth() {
        return month;
    }

    public void setMonth(Integer month) {
        this.month = month;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public Integer getPoints() {
        return points;
    }

    public void setPoints(Integer points) {
        this.points = points;
    }

    public RewardPointDTO() {}

    public RewardPointDTO(Long id, Long customerId, Integer month, Integer year, Integer points) {
        this.id = id;
        this.customerId = customerId;
        this.month = month;
        this.year = year;
        this.points = points;
    }

    @Override
    public String toString() {
        return "RewardPointDTO{" +
                "id=" + id +
                ", customerId=" + customerId +
                ", month=" + month +
                ", year=" + year +
                ", points=" + points +
                '}';
    }
}