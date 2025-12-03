package com.stock.managing.repository.view;

import com.stock.managing.domain.view.VCompanyInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VCompanyInfoRepository extends JpaRepository<VCompanyInfo, String> {
    List<VCompanyInfo> findByNameContaining(String name);

    List<VCompanyInfo> findTop10ByNameContainingOrCodeContaining(String name, String code);

}
