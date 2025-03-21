package com.reward.controller;

import com.reward.Service.RewardPointsService;
import com.reward.dto.CustomerDTO;
import com.reward.dto.RewardPointDTO;
import com.reward.dto.TransactionDTO;
import com.reward.entity.Transaction;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/rewards")
@Tag(name = "RewardPointController",description = "To give the rewards on given Transaction")
public class RewardPointsController {
    private static final Logger logger = LoggerFactory.getLogger(RewardPointsController.class);

    @Autowired
    private RewardPointsService rewardPointsService;
    @Autowired
    private RestTemplate restTemplate;
    @Operation(summary = "Get reward point by customerId ", description = "Fetch Reward point for the customer transaction")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully fetched the Reward point"),
            @ApiResponse(responseCode = "404", description = "Reward Point not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/getReward/{customerId}")
    public ResponseEntity<List<RewardPointDTO>> getRewardPoints(@Parameter(description = "ID of the Customer to be fetch", example = "1")
                                                                @PathVariable Long customerId) {
        logger.info("Fetching reward points for customerId: {}", customerId);
        List<RewardPointDTO> rewardPointDTOs = rewardPointsService.getRewardPoints(customerId);
        logger.info("Reward points fetched: {}", rewardPointDTOs);
        return ResponseEntity.ok(rewardPointDTOs);
    }

    @Operation(summary = "Insert Reward points Calculation on transaction", description = "Add a new transaction reward point to the system")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Successfully add the reward points"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping("/calculate")
    public ResponseEntity<String> calculateRewardPoints(@Parameter(description = "ID of the transaction to be fetched", example = "1")
                                                        @RequestParam Long customerId,
                                                        @Parameter(description = "start Date to be fetch", example = "2025-03-01")
                                                        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
                                                        @Parameter(description = "End Date to be fetch", example = "2025-03-01")
                                                        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        logger.info("Calculating reward points for customerId: {}, startDate: {}, endDate: {}", customerId, startDate, endDate);
        rewardPointsService.calculateRewardPoints(customerId, startDate, endDate);
        logger.info("Reward points calculated for customerId: {}", customerId);
        return ResponseEntity.status(HttpStatus.OK).body("Successfully calculate Reward Points for this customerId: "+customerId);
    }

    @Operation(summary = "Get data by customerId, start date, end date ", description = "Fetch custom data of customer transaction")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully Fetch custom data of customer transaction"),
            @ApiResponse(responseCode = "404", description = "custom data of customer transaction not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/getCustomerTransactionRewardPointDetail")
    public ResponseEntity<Map<String, Object>> getCustomerData(@Parameter(description = "ID of the transaction to be fetched", example = "1")
                                                               @RequestParam Long customerId,
                                                               @Parameter(description = "start Date of the transaction to be fetch", example = "2025-03-01")
                                                               @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
                                                               @Parameter(description = "End Date of the transaction to be fetch", example = "2025-03-31")
                                                               @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        logger.info("Fetching custom data for customerId: {}, startDate: {}, endDate: {}", customerId, startDate, endDate);

        // Fetch customer data
        CustomerDTO customer = restTemplate.getForObject("http://localhost:8080/customers/getCustomerById/" + customerId, CustomerDTO.class);

        // Fetch transaction data
        TransactionDTO[] transactionsArray = restTemplate.getForObject(
                "http://localhost:8080/transactions/getAllTransactions?customerId=" + customerId +
                        "&startDate=" + startDate + "&endDate=" + endDate, TransactionDTO[].class);
        List<TransactionDTO> transactions = Arrays.asList(transactionsArray);

        //customerId is set in each TransactionDTO
        transactions.forEach(transaction -> transaction.setCustomerId(customerId));

        // Fetch reward points data
        RewardPointDTO[] rewardPointsArray = restTemplate.getForObject("http://localhost:8080/rewards/getReward/" + customerId, RewardPointDTO[].class);
        List<RewardPointDTO> rewardPoints = Arrays.asList(rewardPointsArray);

        Map<String, Object> response = new HashMap<>();
        response.put("customer", customer);
        response.put("transactions", transactions);
        response.put("rewardPoints", rewardPoints);

        return ResponseEntity.ok(response);
    }
}
