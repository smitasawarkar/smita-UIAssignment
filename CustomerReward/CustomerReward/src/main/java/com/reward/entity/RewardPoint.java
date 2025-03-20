package com.reward.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class RewardPoint {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long customerId;
    private Integer month;
    private Integer year;
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

    public RewardPoint() {
    }

    public RewardPoint(Long id, Long customerId, Integer month, Integer year, Integer points) {
        this.id = id;
        this.customerId = customerId;
        this.month = month;
        this.year = year;
        this.points = points;
    }

    @Override
    public String toString() {
        return "RewardPoint{" +
                "id=" + id +
                ", customerId=" + customerId +
                ", month=" + month +
                ", year=" + year +
                ", points=" + points +
                '}';
    }
}
