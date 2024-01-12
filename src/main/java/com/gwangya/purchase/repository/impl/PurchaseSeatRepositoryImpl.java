package com.gwangya.purchase.repository.impl;

import org.springframework.stereotype.Repository;

import com.gwangya.performance.domain.Seat;
import com.gwangya.purchase.domain.PurchaseSeat;
import com.gwangya.purchase.repository.PurchaseSeatJpaRepository;
import com.gwangya.purchase.repository.PurchaseSeatRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Repository
public class PurchaseSeatRepositoryImpl implements PurchaseSeatRepository {

	private final PurchaseSeatJpaRepository jpaRepository;

	@Override
	public boolean existsBySeat(Seat seat) {
		return jpaRepository.existsBySeat(seat);
	}

	@Override
	public PurchaseSeat save(PurchaseSeat purchaseSeat) {
		return jpaRepository.save(purchaseSeat);
	}
}
