package com.gwangya.purchase.repository.impl;

import static com.gwangya.purchase.domain.QPurchaseSeat.*;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.gwangya.performance.domain.Seat;
import com.gwangya.purchase.domain.PurchaseSeat;
import com.gwangya.purchase.repository.PurchaseSeatJpaRepository;
import com.gwangya.purchase.repository.PurchaseSeatRepository;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;

import jakarta.annotation.Nonnull;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Repository
public class PurchaseSeatRepositoryImpl implements PurchaseSeatRepository {

	private final PurchaseSeatJpaRepository jpaRepository;

	private final JPAQueryFactory queryFactory;

	@Override
	public boolean existsAnyBySeat(@Nonnull List<Seat> seats) {
		return queryFactory.select(purchaseSeat.id)
			.from(purchaseSeat)
			.where(
				seatIn(seats)
			)
			.fetchOne()
			.describeConstable()
			.isPresent();
	}

	@Override
	public PurchaseSeat save(PurchaseSeat purchaseSeat) {
		return jpaRepository.save(purchaseSeat);
	}

	private BooleanExpression seatIn(List<Seat> seats) {
		return purchaseSeat.seat.in(seats);
	}
}
