package com.gwangya.performance.service;

import java.time.LocalDateTime;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gwangya.global.exception.EntityNotFoundException;
import com.gwangya.performance.domain.PerformanceDetail;
import com.gwangya.performance.dto.PerformanceDetailDto;
import com.gwangya.performance.repository.PerformanceRepository;
import com.gwangya.purchase.repository.PurchaseRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class PerformanceService {

	private final PerformanceRepository performanceRepository;

	private final PurchaseRepository purchaseRepository;

	@Transactional(readOnly = true)
	public PerformanceDetailDto searchPurchasablePerformanceDetailById(final long detailId, final long userId) {
		final PerformanceDetail performanceDetail = performanceRepository.findPerformanceDetailById(detailId)
			.orElseThrow(
				() -> new EntityNotFoundException("존재하지 않는 공연입니다.", PerformanceDetail.class, detailId, userId)
			);
		performanceDetail.checkPurchasePeriod(LocalDateTime.now());

		final long purchasedCount = purchaseRepository.countPurchasedSeat(performanceDetail, userId);
		performanceDetail.checkTicketLimit(purchasedCount);

		return new PerformanceDetailDto(
			performanceDetail.getId(),
			performanceDetail.getPurchaseType(),
			performanceDetail.getLimitCount(),
			performanceDetail.getTicketingStartTime(),
			performanceDetail.getTicketingCloseTime()
		);
	}

	@Transactional(readOnly = true)
	public void checkPurchasablePerformanceDetail(final long detailId, final long userId) {
		final PerformanceDetail performanceDetail = performanceRepository.findPerformanceDetailById(detailId)
			.orElseThrow(
				() -> new EntityNotFoundException("존재하지 않는 공연입니다.", PerformanceDetail.class, detailId, userId)
			);
		performanceDetail.checkPurchasePeriod(LocalDateTime.now());

		final long purchasedCount = purchaseRepository.countPurchasedSeat(performanceDetail, userId);
		performanceDetail.checkTicketLimit(purchasedCount);
	}
}
