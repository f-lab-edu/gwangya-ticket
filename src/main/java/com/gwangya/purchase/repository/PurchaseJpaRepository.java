package com.gwangya.purchase.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.gwangya.purchase.domain.PurchaseInfo;

public interface PurchaseJpaRepository extends JpaRepository<PurchaseInfo, Long> {
}
