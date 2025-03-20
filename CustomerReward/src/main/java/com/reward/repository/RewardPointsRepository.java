package com.reward.repository;

import com.reward.entity.RewardPoint;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RewardPointsRepository extends JpaRepository<RewardPoint, Long> {
    List<RewardPoint> findByCustomerId(Long customerId);
}
