package com.ahmadtrportofolio.pulse.market;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OhlcvRepository extends JpaRepository<Ohlcv, Long> {
    List<Ohlcv> findBySymbolIgnoreCaseAndTimeframeOrderByBarIndexAsc(String symbol, String timeframe);
    long deleteBySymbolIgnoreCaseAndTimeframe(String symbol, String timeframe);
}
