package com.ahmadtrportofolio.pulse.portfolio;

import com.ahmadtrportofolio.pulse.trading.Position;
import com.ahmadtrportofolio.pulse.trading.TradingAccount;
import com.ahmadtrportofolio.pulse.trading.TradingAccountRepository;
import com.ahmadtrportofolio.pulse.trading.TradingService;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.OffsetDateTime;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PortfolioService {

    public record PortfolioSummaryDto(BigDecimal balance, BigDecimal equity, BigDecimal dailyPnl,
                                      BigDecimal dailyPnlPct, BigDecimal marginUsed, BigDecimal freeMargin,
                                      BigDecimal marginLevel) {}

    public record PortfolioStatsDto(BigDecimal winRate, BigDecimal profitFactor, BigDecimal maxDrawdown,
                                    BigDecimal avgWin, BigDecimal avgLoss, long closedTrades) {}

    private final TradingAccountRepository accountRepository;
    private final com.ahmadtrportofolio.pulse.trading.PositionRepository positionRepository;
    private final com.ahmadtrportofolio.pulse.market.SymbolRepository symbolRepository;
    private final EquityCurvePointRepository equityCurvePointRepository;
    private final TransactionRepository transactionRepository;

    public PortfolioService(TradingAccountRepository accountRepository,
                            com.ahmadtrportofolio.pulse.trading.PositionRepository positionRepository,
                            com.ahmadtrportofolio.pulse.market.SymbolRepository symbolRepository,
                            EquityCurvePointRepository equityCurvePointRepository,
                            TransactionRepository transactionRepository) {
        this.accountRepository = accountRepository;
        this.positionRepository = positionRepository;
        this.symbolRepository = symbolRepository;
        this.equityCurvePointRepository = equityCurvePointRepository;
        this.transactionRepository = transactionRepository;
    }

    @Transactional
    public TradingAccount demoAccount() {
        return accountRepository.findAll().stream().findFirst()
                .orElseThrow(() -> new com.ahmadtrportofolio.pulse.common.NotFoundException("Demo trading account not found"));
    }

    @Transactional(readOnly = true)
    public PortfolioSummaryDto summaryFor(TradingAccount account) {
        List<Position> open = positionRepository.findByAccountIdAndStatus(account.getId(), "OPEN");
        BigDecimal marginUsed = BigDecimal.ZERO;
        BigDecimal unrealized = BigDecimal.ZERO;
        for (Position p : open) {
            var symbol = symbolRepository.findByCodeIgnoreCase(p.getSymbol()).orElse(null);
            if (symbol == null) continue;
            marginUsed = marginUsed.add(p.getOpenPrice().multiply(symbol.getContractSize())
                    .multiply(p.getQuantity()).multiply(TradingService.MARGIN_PERCENT));
            unrealized = unrealized.add(pnlOf(p, symbol));
        }
        marginUsed = marginUsed.setScale(2, RoundingMode.HALF_UP);
        unrealized = unrealized.setScale(2, RoundingMode.HALF_UP);
        BigDecimal equity = account.getBalance().add(unrealized);
        BigDecimal freeMargin = equity.subtract(marginUsed);
        BigDecimal marginLevel = marginUsed.signum() == 0 ? BigDecimal.ZERO
                : equity.multiply(BigDecimal.valueOf(100)).divide(marginUsed, 2, RoundingMode.HALF_UP);
        BigDecimal dailyPnlPct = account.getBalance().signum() == 0 ? BigDecimal.ZERO
                : unrealized.multiply(BigDecimal.valueOf(100)).divide(account.getBalance(), 2, RoundingMode.HALF_UP);
        return new PortfolioSummaryDto(account.getBalance(), equity, unrealized, dailyPnlPct, marginUsed, freeMargin, marginLevel);
    }

    private BigDecimal pnlOf(Position p, com.ahmadtrportofolio.pulse.market.Symbol symbol) {
        BigDecimal direction = "BUY".equals(p.getSide()) ? BigDecimal.ONE : BigDecimal.ONE.negate();
        return symbol.getLast().subtract(p.getOpenPrice()).multiply(direction)
                .multiply(p.getQuantity()).multiply(symbol.getContractSize());
    }

    @Transactional
    public PortfolioSummaryDto summary() {
        return summaryFor(demoAccount());
    }

    @Transactional(readOnly = true)
    public List<BigDecimal> equityCurve() {
        TradingAccount account = demoAccount();
        List<BigDecimal> points = equityCurvePointRepository.findByUserIdOrderByPointIndexAsc(account.getUserId())
                .stream().map(EquityCurvePoint::getValue).toList();
        if (points.isEmpty()) {
            return List.of(account.getBalance());
        }
        return points;
    }

    @Transactional
    public void recordTransaction(TradingAccount account, String type, BigDecimal amount, String description) {
        Transaction tx = new Transaction();
        tx.setUserId(account.getUserId());
        tx.setAccountId(account.getId());
        tx.setTxType(type);
        tx.setAmount(amount);
        tx.setCurrency(account.getCurrency());
        tx.setDescription(description);
        tx.setCreatedAt(OffsetDateTime.now());
        transactionRepository.save(tx);
    }

    @Transactional(readOnly = true)
    public List<Transaction> transactionsFor(TradingAccount account) {
        return transactionRepository.findByUserIdOrderByCreatedAtDesc(account.getUserId(),
                org.springframework.data.domain.PageRequest.of(0, 50));
    }

    @Transactional(readOnly = true)
    public PortfolioStatsDto stats() {
        TradingAccount account = demoAccount();
        List<Position> closed = positionRepository.findByAccountIdAndStatus(account.getId(), "CLOSED");
        if (closed.isEmpty()) {
            return new PortfolioStatsDto(BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO,
                    BigDecimal.ZERO, BigDecimal.ZERO, 0);
        }
        long wins = closed.stream().filter(p -> p.getRealizedPnl() != null
                && p.getRealizedPnl().compareTo(BigDecimal.ZERO) > 0).count();
        BigDecimal grossWin = closed.stream().map(p -> p.getRealizedPnl() == null ? BigDecimal.ZERO : p.getRealizedPnl())
                .filter(v -> v.compareTo(BigDecimal.ZERO) > 0).reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal grossLoss = closed.stream().map(p -> p.getRealizedPnl() == null ? BigDecimal.ZERO : p.getRealizedPnl())
                .filter(v -> v.compareTo(BigDecimal.ZERO) < 0).map(BigDecimal::abs).reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal winRate = BigDecimal.valueOf(wins * 100.0 / closed.size()).setScale(1, RoundingMode.HALF_UP);
        BigDecimal profitFactor = grossLoss.signum() == 0 ? grossWin
                : grossWin.divide(grossLoss, 2, RoundingMode.HALF_UP);
        BigDecimal avgWin = wins == 0 ? BigDecimal.ZERO
                : grossWin.divide(BigDecimal.valueOf(wins), 2, RoundingMode.HALF_UP);
        long losses = closed.size() - wins;
        BigDecimal avgLoss = losses == 0 ? BigDecimal.ZERO
                : grossLoss.divide(BigDecimal.valueOf(losses), 2, RoundingMode.HALF_UP);
        return new PortfolioStatsDto(winRate, profitFactor, BigDecimal.ZERO, avgWin, avgLoss, closed.size());
    }
}
