package com.gwangya.performance.repository.impl;

import static com.gwangya.performance.domain.QPerformanceDetail.*;
import static com.gwangya.performance.domain.QSeat.*;
import static com.gwangya.purchase.domain.QPurchaseSeat.*;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.gwangya.performance.domain.PerformanceDetail;
import com.gwangya.performance.domain.Seat;
import com.gwangya.performance.repository.SeatJpaRepository;
import com.gwangya.performance.repository.SeatRepository;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;

import jakarta.annotation.Nonnull;

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

	@Override
	public List<Seat> findAllById(@Nonnull List<Long> seatIds) {
		return jpaRepository.findAllById(seatIds);
	}

	@Override
	public List<Seat> findAllByPerformanceDetail(PerformanceDetail performanceDetail) {
		return jpaRepository.findAllByPerformanceDetail(performanceDetail);
	}

	@Override
	public long countAllByPerformanceDetailId(long performanceDetailId) {
		return jpaQueryFactory.selectFrom(seat)
			.join(seat.performanceDetail, performanceDetail)
			.where(
				performanceDetailIdEq(performanceDetailId)
			)
			.fetch()
			.size();
	}

	private BooleanExpression performanceDetailIdEq(final long detailId) {
		return seat.performanceDetail.id.eq(detailId);
	}
}
