package com.stock.managing.domain.view;

import org.hibernate.annotations.Immutable;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Immutable
@Entity
@Table(name = "v_company_info_us")
@Getter @Setter
public class VCompanyInfoUs {
    @Id
    private String code;
    private String name;
}
