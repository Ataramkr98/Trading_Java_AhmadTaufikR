package com.ahmadtrportofolio.pulse.portfolio;

import java.math.BigDecimal;
import java.util.List;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/portfolio")
public class PortfolioController {

    private final PortfolioService portfolioService;

    public PortfolioController(PortfolioService portfolioService) {
        this.portfolioService = portfolioService;
    }

    @GetMapping("/summary")
    public PortfolioService.PortfolioSummaryDto summary() {
        return portfolioService.summary();
    }

    @GetMapping("/equity-curve")
    public List<BigDecimal> equityCurve() {
        return portfolioService.equityCurve();
    }

    @GetMapping("/transactions")
    public List<Transaction> transactions() {
        var account = portfolioService.demoAccount();
        return portfolioService.transactionsFor(account);
    }

    @GetMapping("/stats")
    public PortfolioService.PortfolioStatsDto stats() {
        return portfolioService.stats();
    }
}
