package com.ahmadtrportofolio.pulse.copy;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Table(name = "copy_subscriptions")
public class CopySubscription {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "follower_id", nullable = false)
    private UUID followerId;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "trader_id", nullable = false)
    private TopTrader trader;

    @Column(name = "copy_mode", nullable = false)
    private String copyMode;

    @Column(name = "fixed_amount", precision = 15, scale = 2)
    private BigDecimal fixedAmount;

    @Column(name = "max_loss_limit", nullable = false, precision = 15, scale = 2)
    private BigDecimal maxLossLimit;

    @Column(name = "max_position_size", nullable = false, precision = 15, scale = 4)
    private BigDecimal maxPositionSize;

    @Column(name = "is_active", nullable = false)
    private boolean active = true;

    @Column(name = "started_at", nullable = false)
    private OffsetDateTime startedAt;

    @Column(name = "stopped_at")
    private OffsetDateTime stoppedAt;

    @Column(name = "total_pnl", precision = 15, scale = 2)
    private BigDecimal totalPnl;

    public UUID getId() { return id; }
    public UUID getFollowerId() { return followerId; }
    public void setFollowerId(UUID followerId) { this.followerId = followerId; }
    public TopTrader getTrader() { return trader; }
    public void setTrader(TopTrader trader) { this.trader = trader; }
    public String getCopyMode() { return copyMode; }
    public void setCopyMode(String copyMode) { this.copyMode = copyMode; }
    public BigDecimal getFixedAmount() { return fixedAmount; }
    public void setFixedAmount(BigDecimal fixedAmount) { this.fixedAmount = fixedAmount; }
    public BigDecimal getMaxLossLimit() { return maxLossLimit; }
    public void setMaxLossLimit(BigDecimal maxLossLimit) { this.maxLossLimit = maxLossLimit; }
    public BigDecimal getMaxPositionSize() { return maxPositionSize; }
    public void setMaxPositionSize(BigDecimal maxPositionSize) { this.maxPositionSize = maxPositionSize; }
    public boolean isActive() { return active; }
    public void setActive(boolean active) { this.active = active; }
    public OffsetDateTime getStartedAt() { return startedAt; }
    public void setStartedAt(OffsetDateTime startedAt) { this.startedAt = startedAt; }
    public OffsetDateTime getStoppedAt() { return stoppedAt; }
    public void setStoppedAt(OffsetDateTime stoppedAt) { this.stoppedAt = stoppedAt; }
    public BigDecimal getTotalPnl() { return totalPnl; }
    public void setTotalPnl(BigDecimal totalPnl) { this.totalPnl = totalPnl; }
}
