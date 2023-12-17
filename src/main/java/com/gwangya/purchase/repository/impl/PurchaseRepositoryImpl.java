package com.gwangya.purchase.repository.impl;

import static com.gwangya.performance.domain.QPerformanceDetail.*;
import static com.gwangya.purchase.domain.QPurchaseInfo.*;
import static com.gwangya.purchase.domain.QPurchaseSeat.*;
import static com.gwangya.user.domain.QUser.*;

import org.springframework.stereotype.Repository;
import org.springframework.util.ObjectUtils;

import com.gwangya.performance.domain.PerformanceDetail;
import com.gwangya.purchase.domain.PurchaseInfo;
import com.gwangya.purchase.repository.PurchaseJpaRepository;
import com.gwangya.purchase.repository.PurchaseRepository;
import com.gwangya.user.domain.User;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;

@Repository
public class PurchaseRepositoryImpl implements PurchaseRepository {

	private final JPAQueryFactory jpaQueryFactory;

	private final PurchaseJpaRepository purchaseJpaRepository;

	public PurchaseRepositoryImpl(JPAQueryFactory jpaQueryFactory, PurchaseJpaRepository purchaseJpaRepository) {
		this.jpaQueryFactory = jpaQueryFactory;
		this.purchaseJpaRepository = purchaseJpaRepository;
	}

	@Override
	public PurchaseInfo save(PurchaseInfo purchaseInfo) {
		return purchaseJpaRepository.save(purchaseInfo);
	}

	@Override
	public long countPurchasedSeatByPerformanceDetailAndUser(PerformanceDetail detail, User requestUser) {
		return jpaQueryFactory.select(purchaseSeat.count())
			.from(purchaseSeat)
			.where(
				userEq(requestUser),
				performanceDetailEq(detail)
			).join(purchaseInfo).on(purchaseSeat.purchaseInfo.eq(purchaseInfo))
			.join(performanceDetail).on(purchaseInfo.performanceDetail.eq(performanceDetail))
			.join(user).on(purchaseInfo.user.eq(user))
			.fetchOne();
	}

	private BooleanExpression performanceDetailEq(final PerformanceDetail detail) {
		return ObjectUtils.isEmpty(detail) ? null : purchaseSeat.purchaseInfo.performanceDetail.eq(detail);
	}

	private BooleanExpression userEq(final User user) {
		return ObjectUtils.isEmpty(user) ? null : purchaseSeat.purchaseInfo.user.eq(user);
	}
}
