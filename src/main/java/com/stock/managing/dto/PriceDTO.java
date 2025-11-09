package com.stock.managing.dto;

import jakarta.persistence.Column;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PriceDTO {
    @Column(name = "date")
    private LocalDate date;
    private double open;
    private double high;
    private double low;
    private double close;
    private long volume;
}