package com.ahmadtrportofolio.pulse.portfolio;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "equity_curve_points")
public class EquityCurvePoint {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false)
    private java.util.UUID userId;

    @Column(name = "point_index", nullable = false)
    private Integer pointIndex;

    @Column(name = "curve_value", nullable = false, precision = 20, scale = 2)
    private BigDecimal value;

    public Long getId() { return id; }
    public java.util.UUID getUserId() { return userId; }
    public void setUserId(java.util.UUID userId) { this.userId = userId; }
    public Integer getPointIndex() { return pointIndex; }
    public void setPointIndex(Integer pointIndex) { this.pointIndex = pointIndex; }
    public BigDecimal getValue() { return value; }
    public void setValue(BigDecimal value) { this.value = value; }
}
