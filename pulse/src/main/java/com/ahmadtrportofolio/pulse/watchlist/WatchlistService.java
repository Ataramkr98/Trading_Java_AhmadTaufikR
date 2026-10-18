package com.ahmadtrportofolio.pulse.watchlist;

import com.ahmadtrportofolio.pulse.common.NotFoundException;
import com.ahmadtrportofolio.pulse.common.OrderRejectedException;
import com.ahmadtrportofolio.pulse.market.SymbolRepository;
import java.util.List;
import java.util.UUID;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class WatchlistService {

    public record WatchlistDto(UUID id, String name, List<String> symbols) {
        static WatchlistDto from(Watchlist w) {
            return new WatchlistDto(w.getId(), w.getName(),
                    w.getItems().stream().map(WatchlistItem::getSymbol).toList());
        }
    }

    private final WatchlistRepository watchlistRepository;
    private final SymbolRepository symbolRepository;
    private final com.ahmadtrportofolio.pulse.portfolio.PortfolioService portfolioService;

    public WatchlistService(WatchlistRepository watchlistRepository, SymbolRepository symbolRepository,
                            com.ahmadtrportofolio.pulse.portfolio.PortfolioService portfolioService) {
        this.watchlistRepository = watchlistRepository;
        this.symbolRepository = symbolRepository;
        this.portfolioService = portfolioService;
    }

    private Watchlist defaultList() {
        return watchlistRepository.findFirstByUserIdOrderByCreatedAtAsc(demoUserId())
                .orElseThrow(() -> new NotFoundException("Watchlist not found"));
    }

    private UUID demoUserId() {
        return portfolioService.demoAccount().getUserId();
    }

    @Transactional
    public WatchlistDto defaultWatchlist() {
        return WatchlistDto.from(defaultList());
    }

    @Transactional
    public void addSymbol(String symbol) {
        Watchlist list = defaultList();
        String code = symbol.toUpperCase();
        boolean exists = list.getItems().stream().anyMatch(i -> i.getSymbol().equalsIgnoreCase(code));
        if (exists) return;
        if (symbolRepository.findByCodeIgnoreCase(code).isEmpty()) {
            throw new OrderRejectedException("Symbol not found: " + symbol);
        }
        WatchlistItem item = new WatchlistItem();
        item.setWatchlist(list);
        item.setSymbol(code);
        item.setItemPosition(list.getItems().size());
        list.getItems().add(item);
        watchlistRepository.save(list);
    }

    @Transactional
    public void removeSymbol(String symbol) {
        Watchlist list = defaultList();
        list.getItems().removeIf(i -> i.getSymbol().equalsIgnoreCase(symbol));
        watchlistRepository.save(list);
    }

    @Transactional(readOnly = true)
    public List<WatchlistDto> all() {
        return watchlistRepository.findByUserIdOrderByCreatedAtAsc(demoUserId()).stream()
                .map(WatchlistDto::from).toList();
    }
}
