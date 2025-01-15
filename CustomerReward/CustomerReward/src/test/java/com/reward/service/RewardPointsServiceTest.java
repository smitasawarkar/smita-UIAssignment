package com.reward.service;

import com.reward.Service.TransactionService;
import com.reward.dto.RewardPointDTO;
import com.reward.entity.RewardPoint;
import com.reward.entity.Transaction;
import com.reward.repository.RewardPointsRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class RewardPointsServiceTest {

    @InjectMocks
    private com.reward.service.RewardPointsService rewardPointsService;

    @Mock
    private RewardPointsRepository rewardPointsRepository;

    @Mock
    private TransactionService transactionService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testCalculateRewardPoints() {
        Long customerId = 1L;
        LocalDate startDate = LocalDate.now().minusDays(10);
        LocalDate endDate = LocalDate.now();

        Transaction transaction1 = new Transaction();
        transaction1.setCustomerId(customerId);
        transaction1.setAmount(120.0);
        transaction1.setDate(LocalDate.now().minusDays(5));

        Transaction transaction2 = new Transaction();
        transaction2.setCustomerId(customerId);
        transaction2.setAmount(80.0);
        transaction2.setDate(LocalDate.now().minusDays(3));

        List<Transaction> transactions = Arrays.asList(transaction1, transaction2);

        when(transactionService.getTransactions(customerId, startDate, endDate)).thenReturn(transactions);

        rewardPointsService.calculateRewardPoints(customerId, startDate, endDate);

        verify(rewardPointsRepository, times(1)).save(any(RewardPoint.class));
    }

    @Test
    public void testGetRewardPoints() {
        Long customerId = 1L;

        RewardPoint rewardPoint1 = new RewardPoint(1L, customerId, 1, 2023, 100);
        RewardPoint rewardPoint2 = new RewardPoint(2L, customerId, 2, 2023, 200);

        List<RewardPoint> rewardPoints = Arrays.asList(rewardPoint1, rewardPoint2);

        when(rewardPointsRepository.findByCustomerId(customerId)).thenReturn(rewardPoints);

        List<RewardPointDTO> result = rewardPointsService.getRewardPoints(customerId);

        assertEquals(2, result.size());
        assertEquals(rewardPoint1.getPoints(), result.get(0).getPoints());
        assertEquals(rewardPoint2.getPoints(), result.get(1).getPoints());
    }
}
