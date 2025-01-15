package com.reward.service;

import com.reward.Service.TransactionService;
import com.reward.dto.RewardPointDTO;
import com.reward.entity.RewardPoint;
import com.reward.entity.Transaction;
import com.reward.repository.RewardPointsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class RewardPointsService {
    private static final Logger logger = LoggerFactory.getLogger(RewardPointsService.class);

    @Autowired
    private RewardPointsRepository rewardPointsRepository;
    @Autowired
    private TransactionService transactionService;

    public void calculateRewardPoints(Long customerId, LocalDate startDate, LocalDate endDate) {
        logger.info("Calculating reward points for customerId: {}, startDate: {}, endDate: {}", customerId, startDate, endDate);

        List<Transaction> transactions = transactionService.getTransactions(customerId, startDate, endDate);
        logger.info("Transactions: {}", transactions);

        int totalPoints = 0;
        for (Transaction transaction : transactions) {
            double amount = transaction.getAmount();
            int points = 0;
            if (amount > 100) {
                points += (amount - 100) * 2;
                amount = 100;
            }
            if (amount > 50) {
                points += (amount - 50);
            }
            totalPoints += points;
        }
        RewardPoint rewardPoints = new RewardPoint();
        rewardPoints.setCustomerId(customerId);
        rewardPoints.setMonth(startDate.getMonthValue());
        rewardPoints.setYear(startDate.getYear());
        rewardPoints.setPoints(totalPoints);
        rewardPointsRepository.save(rewardPoints);

        logger.info("Reward points calculated and saved: {}", rewardPoints);
    }

    public List<RewardPointDTO> getRewardPoints(Long customerId) {
        logger.info("Fetching reward points for customerId: {}", customerId);

        List<RewardPoint> rewardPoints = rewardPointsRepository.findByCustomerId(customerId);
        List<RewardPointDTO> rewardPointDTOs = rewardPoints.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());

        logger.info("Reward points fetched: {}", rewardPointDTOs);
        return rewardPointDTOs;
    }

    private RewardPointDTO convertToDTO(RewardPoint rewardPoint) {
        return new RewardPointDTO(
                rewardPoint.getId(),
                rewardPoint.getCustomerId(),
                rewardPoint.getMonth(),
                rewardPoint.getYear(),
                rewardPoint.getPoints()
        );
    }

    private RewardPoint convertToEntity(RewardPointDTO rewardPointDTO) {
        return new RewardPoint(
                rewardPointDTO.getId(),
                rewardPointDTO.getCustomerId(),
                rewardPointDTO.getMonth(),
                rewardPointDTO.getYear(),
                rewardPointDTO.getPoints()
        );
    }
}