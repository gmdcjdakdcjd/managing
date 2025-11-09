package com.stock.managing.domain.view;

import org.hibernate.annotations.Immutable;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Table(name = "v_daily_price")
@Getter @Setter
@Immutable
@IdClass(VDailyPriceId.class)
public class VDailyPrice {

    @Id
    private String code;

    @Id
    private LocalDate date;

    private Double open;
    private Double high;
    private Double low;
    private Double close;
    private Long volume;
}