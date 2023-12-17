package com.gwangya.performance.repository.impl;

import static com.gwangya.performance.domain.QPerformanceDetail.*;
import static com.gwangya.performance.domain.QSeat.*;
import static com.gwangya.purchase.domain.QPurchaseSeat.*;

import java.util.List;

import org.springframework.stereotype.Repository;
import org.springframework.util.ObjectUtils;

import com.gwangya.performance.domain.PerformanceDetail;
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
	public List<Seat> findRemainingAllByPerformanceDetail(PerformanceDetail detail) {
		return jpaQueryFactory.selectFrom(seat)
			.where(
				performanceDetailEq(detail),
				purchaseSeat.isNull()
			)
			.join(seat.performanceDetail, performanceDetail).fetchJoin()
			.join(purchaseSeat).on(purchaseSeat.seat.eq(seat))
			.fetch();
	}

	private BooleanExpression performanceDetailEq(final PerformanceDetail detail) {
		return ObjectUtils.isEmpty(detail) ? null : seat.performanceDetail.eq(detail);
	}
}
