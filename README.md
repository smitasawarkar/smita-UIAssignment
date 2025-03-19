
public List<Integer> calculateRewardPoints(Long customerId, LocalDate startDate, LocalDate endDate) {
    LOGGER.info("Calculating reward points for customerId {}, startDate {}, endDate {}", customerId, startDate, endDate);

    List<Transaction> transactions = transactionService.getTransactions(customerId, startDate, endDate);
    LOGGER.info("Transactions: {}", transactions);

    List<Integer> transactionPoints = new ArrayList<>();
    int totalPoints = 0;

    for (Transaction transaction : transactions) {
        double amount = transaction.getAmount();
        LOGGER.info("Transaction amount: {}", amount);

        int points = calculatePoints(amount);
        transactionPoints.add(points);  // Store individual transaction points

        totalPoints += points;  // Accumulate total
    }

    Customer customer = customerRepository.findById(customerId)
            .orElseThrow(() -> new RuntimeException("Customer not found"));

    RewardPoint rewardPoints = new RewardPoint();
    rewardPoints.setCustomer(customer);
    rewardPoints.setMonth(startDate.getMonthValue());
    rewardPoints.setYear(startDate.getYear());
    rewardPoints.setPoints(totalPoints);
    rewardPointsRepository.save(rewardPoints);

    LOGGER.info("Reward points calculated for each transaction: {}", transactionPoints);
    LOGGER.info("Total reward points for {}-{}: {}", startDate.getMonth(), startDate.getYear(), totalPoints);

    // Return individual transaction rewards followed by total
    transactionPoints.add(totalPoints);
    return transactionPoints;
}

private int calculatePoints(double amount) {
    int points = 0;
    if (amount > 100) {
        points += (amount - 100) * 2;
        amount = 100;
    }
    if (amount > 50) {
        points += (amount - 50);
    }
    return points;
}
