# smita-UIAssignment

 public List<RewardPointDTO> getRewardPoints(Long customerId) {
        LOGGER.info("Fetching reward points for customerId: {}", customerId);

        List<RewardPoint> rewardPoints = rewardPointsRepository.findByCustomerId(customerId);
        List<RewardPointDTO> rewardPointDTOs = rewardPoints.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());

        LOGGER.info("Reward points fetched: {}", rewardPointDTOs);
        return rewardPointDTOs;
    }
    private RewardPointDTO convertToDTO(RewardPoint rewardPoint) {
        return new RewardPointDTO(
                rewardPoint.getId(),
                rewardPoint.getCustomer().getId(),
                rewardPoint.getMonth(),
                rewardPoint.getYear(),
                rewardPoint.getPoints()
        );
    }
    private RewardPoint convertToEntity(RewardPointDTO rewardPointDTO) {
        Customer customer = customerRepository.findById(rewardPointDTO.getCustomerId())
                .orElseThrow(() -> new RuntimeException("Customer not found"));
        return new RewardPoint(
                rewardPointDTO.getId(),
                customer,
                rewardPointDTO.getMonth(),
                rewardPointDTO.getYear(),
                rewardPointDTO.getPoints()
        );
    }
