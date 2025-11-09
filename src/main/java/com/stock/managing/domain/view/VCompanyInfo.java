package com.stock.managing.domain.view;

import org.hibernate.annotations.Immutable;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Immutable
@Entity
@Table(name = "v_company_info")
@Getter @Setter
public class VCompanyInfo {
    @Id
    private String code;
    private String name;
}

