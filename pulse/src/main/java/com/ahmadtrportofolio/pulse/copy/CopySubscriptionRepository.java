package com.ahmadtrportofolio.pulse.copy;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CopySubscriptionRepository extends JpaRepository<CopySubscription, UUID> {
    List<CopySubscription> findByFollowerIdAndActiveTrue(UUID followerId);
    Optional<CopySubscription> findByFollowerIdAndTraderIdAndActiveTrue(UUID followerId, UUID traderId);
}
