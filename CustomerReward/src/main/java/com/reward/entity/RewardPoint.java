package com.reward.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;

@Entity
public class RewardPoint {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "customer_id", nullable = false)
    @JsonBackReference
    private Customer customer;
    private Integer month;
    private Integer year;
    private Integer points;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
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

    public RewardPoint() {
    }

    public RewardPoint(Long id, Customer customer, Integer month, Integer year, Integer points) {
        this.id = id;
        this.customer = customer;
        this.month = month;
        this.year = year;
        this.points = points;
    }

    @Override
    public String toString() {
        return "RewardPoint{" +
                "id=" + id +
                ", customer=" + customer +
                ", month=" + month +
                ", year=" + year +
                ", points=" + points +
                '}';
    }
}
