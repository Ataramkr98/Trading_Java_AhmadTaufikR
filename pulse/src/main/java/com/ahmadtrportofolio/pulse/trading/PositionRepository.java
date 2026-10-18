package com.ahmadtrportofolio.pulse.trading;

import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PositionRepository extends JpaRepository<Position, UUID> {
    List<Position> findByAccountIdAndStatusOrderByOpenedAtDesc(UUID accountId, String status);
    List<Position> findByAccountIdAndStatus(UUID accountId, String status);
}
