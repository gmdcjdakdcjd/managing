package com.stock.managing.domain;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import java.time.LocalDateTime;

@Entity
@Table(name = "my_stock")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@DynamicInsert   // DEFAULT 값 적용
@DynamicUpdate   // 변경된 필드만 업데이트
public class MyStock {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", length = 50)
    private String userId;

    @Column(name = "code", length = 20, nullable = false)
    private String code;

    @Column(name = "name", length = 50, nullable = false)
    private String name;

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

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "deleted_yn", length = 1)
    private String deletedYn;

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;

    // ====== 자동 세팅용 콜백 ======
    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
        this.deletedYn = (this.deletedYn == null ? "N" : this.deletedYn);
    }

    @PreUpdate
    public void preUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}
