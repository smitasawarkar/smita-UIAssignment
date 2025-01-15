package com.reward.controller;

import com.reward.dto.RewardPointDTO;
import com.reward.service.RewardPointsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/rewards")
public class RewardPointsController {
    private static final Logger logger = LoggerFactory.getLogger(RewardPointsController.class);

    @Autowired
    private RewardPointsService rewardPointsService;

    @GetMapping("/{customerId}")
    public ResponseEntity<List<RewardPointDTO>> getRewardPoints(@PathVariable Long customerId) {
        logger.info("Fetching reward points for customerId: {}", customerId);
        List<RewardPointDTO> rewardPointDTOs = rewardPointsService.getRewardPoints(customerId);
        logger.info("Reward points fetched: {}", rewardPointDTOs);
        return ResponseEntity.ok(rewardPointDTOs);
    }

    @PostMapping("/calculate")
    public ResponseEntity<Void> calculateRewardPoints(@RequestParam Long customerId, @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate, @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        logger.info("Calculating reward points for customerId: {}, startDate: {}, endDate: {}", customerId, startDate, endDate);
        rewardPointsService.calculateRewardPoints(customerId, startDate, endDate);
        logger.info("Reward points calculated for customerId: {}", customerId);
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}