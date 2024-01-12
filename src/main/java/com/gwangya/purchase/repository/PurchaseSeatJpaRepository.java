package com.gwangya.purchase.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.gwangya.performance.domain.Seat;
import com.gwangya.purchase.domain.PurchaseSeat;

public interface PurchaseSeatJpaRepository extends JpaRepository<PurchaseSeat, Long> {

	boolean existsBySeat(Seat seat);
}
