package com.ahmadtrportofolio.pulse.watchlist;

import java.util.List;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/watchlists")
public class WatchlistController {

    private final WatchlistService watchlistService;

    public WatchlistController(WatchlistService watchlistService) {
        this.watchlistService = watchlistService;
    }

    @GetMapping
    public List<WatchlistService.WatchlistDto> all() {
        return watchlistService.all();
    }

    @PostMapping("/items")
    public WatchlistService.WatchlistDto add(@RequestBody AddRequest request) {
        watchlistService.addSymbol(request.symbol());
        return watchlistService.defaultWatchlist();
    }

    @DeleteMapping("/items/{symbol}")
    public WatchlistService.WatchlistDto remove(@PathVariable String symbol) {
        watchlistService.removeSymbol(symbol);
        return watchlistService.defaultWatchlist();
    }

    public record AddRequest(String symbol) {}
}
