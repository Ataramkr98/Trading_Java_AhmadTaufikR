package com.ahmadtrportofolio.pulse.watchlist;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WatchlistRepository extends JpaRepository<Watchlist, UUID> {
    Optional<Watchlist> findFirstByUserIdOrderByCreatedAtAsc(UUID userId);
    List<Watchlist> findByUserIdOrderByCreatedAtAsc(UUID userId);
}
