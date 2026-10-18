package com.ahmadtrportofolio.pulse.portfolio;

import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EquityCurvePointRepository extends JpaRepository<EquityCurvePoint, Long> {
    List<EquityCurvePoint> findByUserIdOrderByPointIndexAsc(UUID userId);
}
