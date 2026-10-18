package com.ahmadtrportofolio.pulse.market;

import java.util.List;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/market")
public class MarketController {

    private final MarketService marketService;

    public MarketController(MarketService marketService) {
        this.marketService = marketService;
    }

    public record QuotesRequest(List<String> symbols) {}

    @GetMapping("/symbols")
    public List<MarketService.SymbolDto> symbols(@RequestParam(required = false) String q) {
        return marketService.search(q);
    }

    @GetMapping("/search")
    public List<MarketService.SymbolDto> search(@RequestParam(required = false) String q) {
        return marketService.search(q);
    }

    @GetMapping("/symbols/{symbol}")
    public MarketService.SymbolDto symbol(@PathVariable String symbol) {
        return marketService.search(symbol).stream()
                .filter(s -> s.symbol().equalsIgnoreCase(symbol))
                .findFirst()
                .orElseThrow(() -> new com.ahmadtrportofolio.pulse.common.NotFoundException("Symbol not found: " + symbol));
    }

    @GetMapping("/quote/{symbol}")
    public MarketService.QuoteDto quote(@PathVariable String symbol) {
        return marketService.getQuote(symbol);
    }

    @PostMapping("/quotes")
    public List<MarketService.QuoteDto> quotes(@RequestBody QuotesRequest request) {
        return marketService.getQuotes(request.symbols() == null ? List.of() : request.symbols());
    }

    @GetMapping("/ohlcv/{symbol}")
    public List<MarketService.CandleDto> ohlcv(@PathVariable String symbol,
                                               @RequestParam(defaultValue = "1m") String tf,
                                               @RequestParam(required = false) Integer limit) {
        return marketService.getCandles(symbol, tf, limit);
    }
}
