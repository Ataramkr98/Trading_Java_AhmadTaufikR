package com.ahmadtrportofolio.pulse.trading;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "account_id", nullable = false)
    private UUID accountId;

    @Column(name = "order_number", nullable = false, unique = true)
    private String orderNumber;

    @Column(name = "symbol", nullable = false)
    private String symbol;

    @Column(name = "side", nullable = false)
    private String side;

    @Column(name = "order_type", nullable = false)
    private String orderType;

    @Column(name = "quantity", nullable = false, precision = 15, scale = 4)
    private BigDecimal quantity;

    @Column(name = "sl_price", precision = 15, scale = 5)
    private BigDecimal slPrice;

    @Column(name = "tp_price", precision = 15, scale = 5)
    private BigDecimal tpPrice;

    @Column(name = "filled_qty", precision = 15, scale = 4)
    private BigDecimal filledQty;

    @Column(name = "avg_fill_price", precision = 15, scale = 5)
    private BigDecimal avgFillPrice;

    @Column(name = "status", nullable = false)
    private String status;

    @Column(name = "reject_reason")
    private String rejectReason;

    @Column(name = "placed_at", nullable = false)
    private OffsetDateTime placedAt;

    @Column(name = "filled_at")
    private OffsetDateTime filledAt;

    public UUID getId() { return id; }
    public UUID getAccountId() { return accountId; }
    public void setAccountId(UUID accountId) { this.accountId = accountId; }
    public String getOrderNumber() { return orderNumber; }
    public void setOrderNumber(String orderNumber) { this.orderNumber = orderNumber; }
    public String getSymbol() { return symbol; }
    public void setSymbol(String symbol) { this.symbol = symbol; }
    public String getSide() { return side; }
    public void setSide(String side) { this.side = side; }
    public String getOrderType() { return orderType; }
    public void setOrderType(String orderType) { this.orderType = orderType; }
    public BigDecimal getQuantity() { return quantity; }
    public void setQuantity(BigDecimal quantity) { this.quantity = quantity; }
    public BigDecimal getSlPrice() { return slPrice; }
    public void setSlPrice(BigDecimal slPrice) { this.slPrice = slPrice; }
    public BigDecimal getTpPrice() { return tpPrice; }
    public void setTpPrice(BigDecimal tpPrice) { this.tpPrice = tpPrice; }
    public BigDecimal getFilledQty() { return filledQty; }
    public void setFilledQty(BigDecimal filledQty) { this.filledQty = filledQty; }
    public BigDecimal getAvgFillPrice() { return avgFillPrice; }
    public void setAvgFillPrice(BigDecimal avgFillPrice) { this.avgFillPrice = avgFillPrice; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public String getRejectReason() { return rejectReason; }
    public void setRejectReason(String rejectReason) { this.rejectReason = rejectReason; }
    public OffsetDateTime getPlacedAt() { return placedAt; }
    public void setPlacedAt(OffsetDateTime placedAt) { this.placedAt = placedAt; }
    public OffsetDateTime getFilledAt() { return filledAt; }
    public void setFilledAt(OffsetDateTime filledAt) { this.filledAt = filledAt; }
}
