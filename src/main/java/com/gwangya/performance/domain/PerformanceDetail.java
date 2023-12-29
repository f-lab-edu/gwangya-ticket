package com.gwangya.performance.domain;

import java.time.LocalDateTime;

import com.gwangya.global.base.BaseEntity;
import com.gwangya.performance.exception.UnavailablePurchaseException;
import com.gwangya.purchase.domain.PurchaseType;
import com.gwangya.purchase.repository.PurchaseRepository;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "PERFORMANCE_DETAIL")
public class PerformanceDetail extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne
	@JoinColumn(name = "performance_id")
	private Performance performance;

	@Column(name = "purchase_type")
	@Enumerated(EnumType.STRING)
	private PurchaseType purchaseType;

	@Column(name = "ticket_limit_count")
	private int limitCount;

	@Column(name = "ticketing_start_datetime")
	private LocalDateTime ticketingStartTime;

	@Column(name = "ticketing_close_datetime")
	private LocalDateTime ticketingCloseTime;

	public PerformanceDetail(Long id, Performance performance, PurchaseType purchaseType, int limitCount,
		LocalDateTime ticketingStartTime, LocalDateTime ticketingCloseTime, LocalDateTime createdAt,
		LocalDateTime updatedAt) {
		super(createdAt, updatedAt);
		this.id = id;
		this.performance = performance;
		this.purchaseType = purchaseType;
		this.limitCount = limitCount;
		this.ticketingStartTime = ticketingStartTime;
		this.ticketingCloseTime = ticketingCloseTime;
	}

	public void checkPurchasePeriod(final LocalDateTime now) {
		if (now.isBefore(ticketingStartTime) || now.isAfter(ticketingCloseTime)) {
			throw new UnavailablePurchaseException("예매 가능 기간이 아닙니다.");
		}
	}

	public void checkTicketLimit(final PurchaseRepository purchaseRepository, final Long userId) {
		long purchasedCount = purchaseRepository.countPurchasedSeatByPerformanceDetailAndUserId(this, userId);
		if (purchasedCount >= limitCount) {
			throw new UnavailablePurchaseException("예매 가능 매수를 초과했습니다.");
		}
	}
}
