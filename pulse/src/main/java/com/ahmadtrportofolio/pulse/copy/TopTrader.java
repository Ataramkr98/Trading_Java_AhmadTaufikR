package com.ahmadtrportofolio.pulse.copy;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name = "top_traders")
public class TopTrader {
    @Id
    private UUID id;

    @Column(name = "display_name", nullable = false)
    private String displayName;

    @Column(name = "style", nullable = false)
    private String style;

    @Column(name = "roi", nullable = false, precision = 8, scale = 2)
    private BigDecimal roi;

    @Column(name = "win_rate", nullable = false, precision = 5, scale = 2)
    private BigDecimal winRate;

    @Column(name = "followers", nullable = false)
    private int followers;

    @Column(name = "pulse_score", nullable = false, precision = 5, scale = 2)
    private BigDecimal pulseScore;

    @Column(name = "drawdown", nullable = false, precision = 5, scale = 2)
    private BigDecimal drawdown;

    @Column(name = "is_verified", nullable = false)
    private boolean verified;

    @Column(name = "months_tracked", nullable = false)
    private int monthsTracked;

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }
    public String getDisplayName() { return displayName; }
    public void setDisplayName(String displayName) { this.displayName = displayName; }
    public String getStyle() { return style; }
    public void setStyle(String style) { this.style = style; }
    public BigDecimal getRoi() { return roi; }
    public void setRoi(BigDecimal roi) { this.roi = roi; }
    public BigDecimal getWinRate() { return winRate; }
    public void setWinRate(BigDecimal winRate) { this.winRate = winRate; }
    public int getFollowers() { return followers; }
    public void setFollowers(int followers) { this.followers = followers; }
    public BigDecimal getPulseScore() { return pulseScore; }
    public void setPulseScore(BigDecimal pulseScore) { this.pulseScore = pulseScore; }
    public BigDecimal getDrawdown() { return drawdown; }
    public void setDrawdown(BigDecimal drawdown) { this.drawdown = drawdown; }
    public boolean isVerified() { return verified; }
    public void setVerified(boolean verified) { this.verified = verified; }
    public int getMonthsTracked() { return monthsTracked; }
    public void setMonthsTracked(int monthsTracked) { this.monthsTracked = monthsTracked; }
}
