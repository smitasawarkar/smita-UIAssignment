public List<RewardPointDTO> getRewardPoints(Long customerId) {
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
    if (rewardPoint == null || rewardPoint.getCustomer() == null) {
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
            rewardPointDTO.getId(),
            customer,
            rewardPointDTO.getMonth(),
            rewardPointDTO.getYear(),
            rewardPointDTO.getPoints()
    );
}
