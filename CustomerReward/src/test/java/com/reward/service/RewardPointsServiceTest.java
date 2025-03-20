package com.reward.service;
import com.reward.Service.RewardPointsService;
import com.reward.Service.TransactionService;
import com.reward.dto.RewardPointDTO;
import com.reward.entity.Customer;
import com.reward.entity.RewardPoint;
import com.reward.entity.Transaction;
import com.reward.exception.ResourceNotFoundException;
import com.reward.repository.CustomerRepository;
import com.reward.repository.RewardPointsRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.time.Month;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class RewardPointsServiceTest {

    @InjectMocks
    private RewardPointsService rewardPointsService;

    @Mock
    private RewardPointsRepository rewardPointsRepository;

    @Mock
    private TransactionService transactionService;

    @Mock
    private CustomerRepository customerRepository;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testCalculateRewardPoints() {
        Long customerId = 1L;
        LocalDate startDate = LocalDate.of(2025, 3, 1);
        LocalDate endDate = LocalDate.of(2025, 3, 31);
        List<Transaction> transactions = Arrays.asList(new Transaction());
        Customer customer = new Customer();
        customer.setId(customerId);

        when(transactionService.getTransactions(customerId, startDate, endDate)).thenReturn(transactions);
        when(customerRepository.findById(customerId)).thenReturn(Optional.of(customer));

        rewardPointsService.calculateRewardPoints(customerId, startDate, endDate);

        verify(transactionService, times(1)).getTransactions(customerId, startDate, endDate);
        verify(customerRepository, times(1)).findById(customerId);
        verify(rewardPointsRepository, times(transactions.size())).save(any(RewardPoint.class));
    }

    @Test
    public void testCalculateRewardPointsNoTransactions() {
        Long customerId = 1L;
        LocalDate startDate = LocalDate.of(2025, 3, 1);
        LocalDate endDate = LocalDate.of(2025, 3, 31);

        when(transactionService.getTransactions(customerId, startDate, endDate)).thenReturn(Collections.emptyList());

        rewardPointsService.calculateRewardPoints(customerId, startDate, endDate);

        verify(transactionService, times(1)).getTransactions(customerId, startDate, endDate);
        verify(customerRepository, never()).findById(customerId);
        verify(rewardPointsRepository, never()).save(any(RewardPoint.class));
    }

    @Test
    public void testGetRewardPoints() {
        Long customerId = 1L;
        List<RewardPoint> rewardPoints = Arrays.asList(new RewardPoint());
        when(rewardPointsRepository.findByCustomerId(customerId)).thenReturn(rewardPoints);

        List<RewardPointDTO> result = rewardPointsService.getRewardPoints(customerId);

        assertEquals(rewardPoints.size(), result.size());
        verify(rewardPointsRepository, times(1)).findByCustomerId(customerId);
    }

    @Test
    public void testGetRewardPointsNoPoints() {
        Long customerId = 1L;
        when(rewardPointsRepository.findByCustomerId(customerId)).thenReturn(Collections.emptyList());

        List<RewardPointDTO> result = rewardPointsService.getRewardPoints(customerId);

        assertTrue(result.isEmpty());
        verify(rewardPointsRepository, times(1)).findByCustomerId(customerId);
    }

//    @Test
//    public void testConvertToDTO() {
//        RewardPoint rewardPoint = new RewardPoint();
//        Customer customer = new Customer();
//        customer.setId(1L);
//        rewardPoint.setCustomer(customer);
//        rewardPoint.setId(1L);
//        rewardPoint.setMonth(3);
//        rewardPoint.setYear(2025);
//        rewardPoint.setPoints(100);
//
//        RewardPointDTO result = rewardPointsService.convertToDTO(rewardPoint);
//
//        assertEquals(rewardPoint.getId(), result.getId());
//        assertEquals(rewardPoint.getCustomer().getId(), result.getCustomerId());
//        assertEquals(rewardPoint.getMonth(), result.getMonth());
//        assertEquals(rewardPoint.getYear(), result.getYear());
//        assertEquals(rewardPoint.getPoints(), result.getPoints());
//    }
//
//    @Test
//    public void testConvertToDTOInvalidData() {
//        RewardPoint rewardPoint = new RewardPoint();
//        // Setting customer to null to trigger the exception
//        rewardPoint.setCustomer(null);
//
//        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
//            rewardPointsService.convertToDTO(rewardPoint);
//        });
//
//        assertEquals("Invalid reward point data", exception.getMessage());
//    }
}
