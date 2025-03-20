package com.reward.controller;


import com.reward.dto.RewardPointDTO;
import com.reward.service.RewardPointsService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class RewardPointsControllerTest {

    @InjectMocks
    private RewardPointsController rewardPointsController;

    @Mock
    private RewardPointsService rewardPointsService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetRewardPoints() {
        Long customerId = 1L;
        RewardPointDTO rewardPointDTO1 = new RewardPointDTO(1L, customerId, 1, 2023, 100);
        RewardPointDTO rewardPointDTO2 = new RewardPointDTO(2L, customerId, 2, 2023, 200);

        List<RewardPointDTO> rewardPointDTOs = Arrays.asList(rewardPointDTO1, rewardPointDTO2);

        when(rewardPointsService.getRewardPoints(customerId)).thenReturn(rewardPointDTOs);

        ResponseEntity<List<RewardPointDTO>> response = rewardPointsController.getRewardPoints(customerId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(rewardPointDTOs, response.getBody());
    }

    @Test
    public void testCalculateRewardPoints() {
        Long customerId = 1L;
        LocalDate startDate = LocalDate.now().minusDays(10);
        LocalDate endDate = LocalDate.now();

        doNothing().when(rewardPointsService).calculateRewardPoints(customerId, startDate, endDate);

        ResponseEntity<Void> response = rewardPointsController.calculateRewardPoints(customerId, startDate, endDate);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(rewardPointsService, times(1)).calculateRewardPoints(customerId, startDate, endDate);
    }
}
