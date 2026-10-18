package com.ahmadtrportofolio.pulse.market;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "ohlcv")
public class Ohlcv {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "symbol", nullable = false)
    private String symbol;

    @Column(name = "timeframe", nullable = false)
    private String timeframe;

    @Column(name = "bar_index", nullable = false)
    private Integer barIndex;

    @Column(name = "open", nullable = false, precision = 15, scale = 5)
    private BigDecimal open;

    @Column(name = "high", nullable = false, precision = 15, scale = 5)
    private BigDecimal high;

    @Column(name = "low", nullable = false, precision = 15, scale = 5)
    private BigDecimal low;

    @Column(name = "close", nullable = false, precision = 15, scale = 5)
    private BigDecimal close;

    public Long getId() { return id; }
    public String getSymbol() { return symbol; }
    public void setSymbol(String symbol) { this.symbol = symbol; }
    public String getTimeframe() { return timeframe; }
    public void setTimeframe(String timeframe) { this.timeframe = timeframe; }
    public Integer getBarIndex() { return barIndex; }
    public void setBarIndex(Integer barIndex) { this.barIndex = barIndex; }
    public BigDecimal getOpen() { return open; }
    public void setOpen(BigDecimal open) { this.open = open; }
    public BigDecimal getHigh() { return high; }
    public void setHigh(BigDecimal high) { this.high = high; }
    public BigDecimal getLow() { return low; }
    public void setLow(BigDecimal low) { this.low = low; }
    public BigDecimal getClose() { return close; }
    public void setClose(BigDecimal close) { this.close = close; }
}
