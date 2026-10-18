package com.ahmadtrportofolio.pulse.market;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "symbols")
public class Symbol {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private java.util.UUID id;

    @Column(name = "code", nullable = false, unique = true)
    private String code;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "category", nullable = false)
    private String category;

    @Column(name = "digits", nullable = false)
    private int digits;

    @Column(name = "min_lot", nullable = false, precision = 10, scale = 4)
    private BigDecimal minLot;

    @Column(name = "max_lot", precision = 15, scale = 4)
    private BigDecimal maxLot;

    @Column(name = "contract_size", nullable = false, precision = 15, scale = 4)
    private BigDecimal contractSize;

    @Column(name = "bid", nullable = false, precision = 15, scale = 5)
    private BigDecimal bid;

    @Column(name = "ask", nullable = false, precision = 15, scale = 5)
    private BigDecimal ask;

    @Column(name = "last", nullable = false, precision = 15, scale = 5)
    private BigDecimal last;

    @Column(name = "change_val", precision = 15, scale = 5)
    private BigDecimal changeVal;

    @Column(name = "change_pct", precision = 10, scale = 4)
    private BigDecimal changePct;

    @Column(name = "spread_pips", precision = 10, scale = 2)
    private BigDecimal spreadPips;

    @Column(name = "volume_label")
    private String volumeLabel;

    @Column(name = "market_status")
    private String marketStatus;

    public java.util.UUID getId() { return id; }
    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }
    public int getDigits() { return digits; }
    public void setDigits(int digits) { this.digits = digits; }
    public BigDecimal getMinLot() { return minLot; }
    public void setMinLot(BigDecimal minLot) { this.minLot = minLot; }
    public BigDecimal getMaxLot() { return maxLot; }
    public void setMaxLot(BigDecimal maxLot) { this.maxLot = maxLot; }
    public BigDecimal getContractSize() { return contractSize; }
    public void setContractSize(BigDecimal contractSize) { this.contractSize = contractSize; }
    public BigDecimal getBid() { return bid; }
    public void setBid(BigDecimal bid) { this.bid = bid; }
    public BigDecimal getAsk() { return ask; }
    public void setAsk(BigDecimal ask) { this.ask = ask; }
    public BigDecimal getLast() { return last; }
    public void setLast(BigDecimal last) { this.last = last; }
    public BigDecimal getChangeVal() { return changeVal; }
    public void setChangeVal(BigDecimal changeVal) { this.changeVal = changeVal; }
    public BigDecimal getChangePct() { return changePct; }
    public void setChangePct(BigDecimal changePct) { this.changePct = changePct; }
    public BigDecimal getSpreadPips() { return spreadPips; }
    public void setSpreadPips(BigDecimal spreadPips) { this.spreadPips = spreadPips; }
    public String getVolumeLabel() { return volumeLabel; }
    public void setVolumeLabel(String volumeLabel) { this.volumeLabel = volumeLabel; }
    public String getMarketStatus() { return marketStatus; }
    public void setMarketStatus(String marketStatus) { this.marketStatus = marketStatus; }
}
