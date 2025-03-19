# smita-UIAssignment

@Service
public class RewardPointsService {
    private static final Logger LOGGER = LoggerFactory.getLogger(RewardPointsService.class);

    @Autowired
    private TransactionService transactionService;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private RewardPointRepository rewardPointsRepository;

    public void calculateRewardPoints(Long customerId, LocalDate startDate, LocalDate endDate) {
        LOGGER.info("Calculating reward points for customerId: {}, startDate: {}, endDate: {}", customerId, startDate, endDate);

        List<Transaction> transactions = transactionService.getTransactions(customerId, startDate, endDate);
        if (transactions.isEmpty()) {
            LOGGER.warn("No transactions found for customerId: {}", customerId);
            return;
        }

        LOGGER.debug("Transactions count: {}", transactions.size());

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
                t -> t.getTransactionDate().getMonth(),
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
}
