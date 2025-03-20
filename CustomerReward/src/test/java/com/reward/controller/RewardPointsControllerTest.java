package com.reward.controller;
import com.reward.Service.RewardPointsService;
import com.reward.dto.CustomerDTO;
import com.reward.dto.RewardPointDTO;
import com.reward.dto.TransactionDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class RewardPointsControllerTest {

    @InjectMocks
    private RewardPointsController rewardPointsController;

    @Mock
    private RewardPointsService rewardPointsService;

    @Mock
    private RestTemplate restTemplate;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetRewardPoints() {
        Long customerId = 1L;
        List<RewardPointDTO> rewardPoints = Arrays.asList(new RewardPointDTO());
        when(rewardPointsService.getRewardPoints(customerId)).thenReturn(rewardPoints);

        ResponseEntity<List<RewardPointDTO>> response = rewardPointsController.getRewardPoints(customerId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(rewardPoints, response.getBody());
        verify(rewardPointsService, times(1)).getRewardPoints(customerId);
    }

    @Test
    public void testCalculateRewardPoints() {
        Long customerId = 1L;
        LocalDate startDate = LocalDate.of(2025, 3, 1);
        LocalDate endDate = LocalDate.of(2025, 3, 31);

        ResponseEntity<String> response = rewardPointsController.calculateRewardPoints(customerId, startDate, endDate);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Successfully calculate Reward Points for this customerId: " + customerId, response.getBody());
        verify(rewardPointsService, times(1)).calculateRewardPoints(customerId, startDate, endDate);
    }

    @Test
    public void testGetCustomerData() {
        Long customerId = 1L;
        LocalDate startDate = LocalDate.of(2025, 3, 1);
        LocalDate endDate = LocalDate.of(2025, 3, 31);
        CustomerDTO customer = new CustomerDTO();
        TransactionDTO[] transactionsArray = new TransactionDTO[]{new TransactionDTO()};
        RewardPointDTO[] rewardPointsArray = new RewardPointDTO[]{new RewardPointDTO()};

        when(restTemplate.getForObject("http://localhost:8080/customers/getCustomerById/" + customerId, CustomerDTO.class)).thenReturn(customer);
        when(restTemplate.getForObject("http://localhost:8080/transactions/getAllTransactions?customerId=" + customerId + "&startDate=" + startDate + "&endDate=" + endDate, TransactionDTO[].class)).thenReturn(transactionsArray);
        when(restTemplate.getForObject("http://localhost:8080/rewards/getReward/" + customerId, RewardPointDTO[].class)).thenReturn(rewardPointsArray);

        ResponseEntity<Map<String, Object>> response = rewardPointsController.getCustomerData(customerId, startDate, endDate);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(customer, response.getBody().get("customer"));
        assertEquals(Arrays.asList(transactionsArray), response.getBody().get("transactions"));
        assertEquals(Arrays.asList(rewardPointsArray), response.getBody().get("rewardPoints"));
        verify(restTemplate, times(1)).getForObject("http://localhost:8080/customers/getCustomerById/" + customerId, CustomerDTO.class);
        verify(restTemplate, times(1)).getForObject("http://localhost:8080/transactions/getAllTransactions?customerId=" + customerId + "&startDate=" + startDate + "&endDate=" + endDate, TransactionDTO[].class);
        verify(restTemplate, times(1)).getForObject("http://localhost:8080/rewards/getReward/" + customerId, RewardPointDTO[].class);
    }
}
