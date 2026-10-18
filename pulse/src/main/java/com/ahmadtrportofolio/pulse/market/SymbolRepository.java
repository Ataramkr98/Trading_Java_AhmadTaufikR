package com.ahmadtrportofolio.pulse.market;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SymbolRepository extends JpaRepository<Symbol, java.util.UUID> {
    Optional<Symbol> findByCodeIgnoreCase(String code);
    List<Symbol> findByCodeContainingIgnoreCaseOrNameContainingIgnoreCase(String code, String name);
    List<Symbol> findAllByOrderByCodeAsc();
}
