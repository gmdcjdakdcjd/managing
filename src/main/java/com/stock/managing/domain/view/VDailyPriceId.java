package com.stock.managing.domain.view;

import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.time.LocalDate;

@EqualsAndHashCode
public class VDailyPriceId implements Serializable {
    private String code;
    private LocalDate date;
}