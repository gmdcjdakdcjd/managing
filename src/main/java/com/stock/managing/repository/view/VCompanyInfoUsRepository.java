package com.stock.managing.repository.view;

import com.stock.managing.domain.view.VCompanyInfoUs;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VCompanyInfoUsRepository extends JpaRepository<VCompanyInfoUs, String> {
    List<VCompanyInfoUs> findByNameContaining(String name);
}
