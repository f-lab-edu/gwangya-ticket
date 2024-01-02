package com.gwangya.performance.repository.impl;

import static com.gwangya.performance.domain.QPerformanceDetail.*;
import static com.gwangya.performance.domain.QSeat.*;
import static com.gwangya.purchase.domain.QPurchaseSeat.*;

import java.util.List;

import org.springframework.stereotype.Repository;
import org.springframework.util.ObjectUtils;

import com.gwangya.performance.domain.Seat;
import com.gwangya.performance.repository.SeatJpaRepository;
import com.gwangya.performance.repository.SeatRepository;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;

@Repository
public class SeatRepositoryImpl implements SeatRepository {

	private final SeatJpaRepository jpaRepository;

	private final JPAQueryFactory jpaQueryFactory;

	public SeatRepositoryImpl(SeatJpaRepository jpaRepository, JPAQueryFactory jpaQueryFactory) {
		this.jpaRepository = jpaRepository;
		this.jpaQueryFactory = jpaQueryFactory;
	}

	@Override
	public List<Seat> findRemainingAllByPerformanceDetailId(long detailId) {
		return jpaQueryFactory.selectFrom(seat)
			.where(
				performanceDetailIdEq(detailId),
				purchaseSeat.isNull()
			)
			.join(seat.performanceDetail, performanceDetail).fetchJoin()
			.join(purchaseSeat).on(purchaseSeat.seat.eq(seat))
			.fetch();
	}

	private BooleanExpression performanceDetailIdEq(final long detailId) {
		return ObjectUtils.isEmpty(detailId) ? null : seat.performanceDetail.id.eq(detailId);
	}
}
