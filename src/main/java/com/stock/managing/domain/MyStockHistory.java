package com.stock.managing.domain;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import java.time.LocalDateTime;

@Entity
@Table(name = "my_stock_history")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@DynamicInsert
@DynamicUpdate
public class MyStockHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "hist_id")
    private Long histId;

    @Column(name = "my_stock_id", nullable = false)
    private Long myStockId;

    @Column(name = "user_id", length = 50)
    private String userId;

    @Column(name = "code", length = 20, nullable = false)
    private String code;

    @Column(name = "name", length = 50, nullable = false)
    private String name;

    @Column(name = "action", length = 50, nullable = false)
    private String action; // ADD / UPDATE / DELETE / RESTORE

    @Column(name = "strategy_name", length = 100)
    private String strategyName;

    @Column(name = "special_value")
    private Double specialValue;

    @Column(name = "price_at_add")
    private Double priceAtAdd;

    @Column(name = "target_price_5")
    private Double targetPrice5;

    @Column(name = "target_price_10")
    private Double targetPrice10;

    @Column(name = "memo", length = 500)
    private String memo;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "note", length = 300)
    private String note;

    // ===== 자동 생성 =====
    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
    }
}
