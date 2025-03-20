package com.reward.Service;
import com.reward.dto.RewardPointDTO;
import com.reward.entity.Customer;
import com.reward.entity.RewardPoint;
import com.reward.entity.Transaction;
import com.reward.exception.ResourceNotFoundException;
import com.reward.repository.CustomerRepository;
import com.reward.repository.RewardPointsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
@Service
public class RewardPointsService {
    private static final Logger LOGGER = LoggerFactory.getLogger(RewardPointsService.class);
    @Autowired
    private RewardPointsRepository rewardPointsRepository;
    @Autowired
    private TransactionService transactionService;
    @Autowired
    private CustomerRepository customerRepository;

    public void calculateRewardPoints(Long customerId, LocalDate startDate, LocalDate endDate) {
        LOGGER.info("Calculating reward points for customerId: {}, startDate: {}, endDate: {}", customerId, startDate, endDate);
        List<Transaction> transactions = transactionService.getTransactions(customerId, startDate, endDate);
        if (transactions.isEmpty()) {
            LOGGER.warn("No transactions found for customerId: {}", customerId);
            return;
        }LOGGER.debug("Transactions count: {}", transactions.size());
        // Calculate monthly reward points
        Map<Month, Integer> monthlyPoints = calculateMonthlyPoints(transactions);
        int totalPoints = calculateTotalPoints(transactions);
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found with ID: " + customerId));
        for (Map.Entry<Month, Integer> entry : monthlyPoints.entrySet()) {
            RewardPoint rewardPoints = new RewardPoint();
            rewardPoints.setCustomer(customer);
            rewardPoints.setMonth(entry.getKey().getValue());
            rewardPoints.setYear(startDate.getYear());
            rewardPoints.setPoints(entry.getValue());
            rewardPointsRepository.save(rewardPoints);
        }
        LOGGER.info("Total reward points for customer {}: {}", customerId, totalPoints);
    }
    public Map<Month, Integer> calculateMonthlyPoints(List<Transaction> transactions) {
        return transactions.stream()
                .collect(Collectors.groupingBy(
                        t -> t.getDate().getMonth(),
                        Collectors.summingInt(t -> calculatePoints(t.getAmount()))
                ));
    }
    public int calculateTotalPoints(List<Transaction> transactions) {
        return transactions.stream()
                .mapToInt(t -> calculatePoints(t.getAmount()))
                .sum();
    }

    private int calculatePoints(double amount) {
        int points = 0;
        if (amount > 100) {
            points += (int) ((amount - 100) * 2); // 2 points per $ above 100
            amount = 100;
        }
        if (amount > 50) {
            points += (int) (amount - 50); // 1 point per $ between 50-100
        }
        return points;
    }
//===========================================================================================================

    public List getRewardPoints(Long customerId) {
        LOGGER.info("Fetching reward points for customerId: {}", customerId);
        List<RewardPoint> rewardPoints = rewardPointsRepository.findByCustomerId(customerId);
        if (rewardPoints.isEmpty()) {
            LOGGER.warn("No reward points found for customerId: {}", customerId);
            return Collections.emptyList();
        }

        List<RewardPointDTO> rewardPointDTOs = rewardPoints.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
        LOGGER.info("Reward points fetched: {} entries", rewardPointDTOs.size());
        return rewardPointDTOs;
    }

    private RewardPointDTO convertToDTO(RewardPoint rewardPoint) {
        if (rewardPoint == null || rewardPoint.getCustomer() == null)
        {
            throw new IllegalArgumentException("Invalid reward point data");
        }
        return new RewardPointDTO(
                rewardPoint.getId(),
                rewardPoint.getCustomer().getId(),
                rewardPoint.getMonth(),
                rewardPoint.getYear(),
                rewardPoint.getPoints()
        );
    }

// Use only if you need to save/update reward points
private RewardPoint convertToEntity(RewardPointDTO rewardPointDTO) {
        Customer customer = customerRepository.findById(rewardPointDTO.getCustomerId())
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found with ID: " + rewardPointDTO.getCustomerId()));
        return new RewardPoint(
            rewardPointDTO.getId(),customer,
            rewardPointDTO.getMonth(),
            rewardPointDTO.getYear(),
            rewardPointDTO.getPoints()
            );
}





}