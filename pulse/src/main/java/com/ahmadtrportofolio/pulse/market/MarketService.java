package com.ahmadtrportofolio.pulse.market;

import com.ahmadtrportofolio.pulse.common.NotFoundException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MarketService {

    public record SymbolDto(String symbol, String name, String category, int digits,
                            BigDecimal minLot, BigDecimal contractSize) {
        static SymbolDto from(Symbol s) {
            return new SymbolDto(s.getCode(), s.getName(), s.getCategory(), s.getDigits(), s.getMinLot(), s.getContractSize());
        }
    }

    public record QuoteDto(String symbol, String name, String category, int digits,
                           BigDecimal minLot, BigDecimal contractSize,
                           BigDecimal bid, BigDecimal ask, BigDecimal last,
                           BigDecimal change, BigDecimal changePct, BigDecimal spread,
                           String volume, String marketStatus, String updatedAt) {
        static QuoteDto from(Symbol s) {
            return new QuoteDto(s.getCode(), s.getName(), s.getCategory(), s.getDigits(), s.getMinLot(),
                    s.getContractSize(), s.getBid(), s.getAsk(), s.getLast(), s.getChangeVal(), s.getChangePct(),
                    s.getSpreadPips(), s.getVolumeLabel(), s.getMarketStatus(), "LIVE");
        }
    }

    public record CandleDto(BigDecimal open, BigDecimal high, BigDecimal low, BigDecimal close) {
        static CandleDto from(Ohlcv c) {
            return new CandleDto(c.getOpen(), c.getHigh(), c.getLow(), c.getClose());
        }
    }

    private final SymbolRepository symbolRepository;
    private final OhlcvRepository ohlcvRepository;

    public MarketService(SymbolRepository symbolRepository, OhlcvRepository ohlcvRepository) {
        this.symbolRepository = symbolRepository;
        this.ohlcvRepository = ohlcvRepository;
    }

    @Transactional(readOnly = true)
    public List<QuoteDto> getQuotes(List<String> symbols) {
        return symbols.stream()
                .map(symbolRepository::findByCodeIgnoreCase)
                .flatMap(Optional::stream)
                .map(QuoteDto::from)
                .toList();
    }

    @Transactional(readOnly = true)
    public QuoteDto getQuote(String symbol) {
        return symbolRepository.findByCodeIgnoreCase(symbol)
                .map(QuoteDto::from)
                .orElseThrow(() -> new NotFoundException("Symbol not found: " + symbol));
    }

    @Transactional(readOnly = true)
    public List<CandleDto> getCandles(String symbol, String timeframe, Integer limit) {
        List<Ohlcv> bars = ohlcvRepository.findBySymbolIgnoreCaseAndTimeframeOrderByBarIndexAsc(symbol, timeframe);
        if (bars.isEmpty()) {
            throw new NotFoundException("No candle data for " + symbol + " timeframe " + timeframe);
        }
        var mapped = bars.stream().map(CandleDto::from).toList();
        if (limit != null && limit > 0 && limit < mapped.size()) {
            return mapped.subList(mapped.size() - limit, mapped.size());
        }
        return mapped;
    }

    @Transactional(readOnly = true)
    public List<SymbolDto> search(String query) {
        if (query == null || query.isBlank()) {
            return symbolRepository.findAllByOrderByCodeAsc().stream().map(SymbolDto::from).toList();
        }
        return symbolRepository.findByCodeContainingIgnoreCaseOrNameContainingIgnoreCase(query, query)
                .stream().map(SymbolDto::from).toList();
    }

    public static BigDecimal round(BigDecimal value, int digits) {
        return value.setScale(digits, RoundingMode.HALF_UP);
    }
}
