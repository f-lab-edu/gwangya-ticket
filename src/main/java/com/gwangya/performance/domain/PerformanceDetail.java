package com.gwangya.performance.domain;

import static com.gwangya.performance.exception.UnavailablePurchaseType.*;

import java.time.LocalDateTime;

import com.gwangya.global.base.BaseEntity;
import com.gwangya.performance.exception.UnavailablePurchaseException;
import com.gwangya.purchase.domain.PurchaseType;

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

	@Column(name = "purchase_type", nullable = false)
	@Enumerated(EnumType.STRING)
	private PurchaseType purchaseType;

	@Column(name = "ticket_limit_count", nullable = false)
	private int limitCount;

	@Column(name = "ticketing_start_datetime", nullable = false)
	private LocalDateTime ticketingStartTime;

	@Column(name = "ticketing_close_datetime", nullable = false)
	private LocalDateTime ticketingCloseTime;

	public PerformanceDetail(Performance performance, PurchaseType purchaseType, int limitCount,
		LocalDateTime ticketingStartTime, LocalDateTime ticketingCloseTime, LocalDateTime createdAt,
		LocalDateTime updatedAt) {
		super(createdAt, updatedAt);
		this.performance = performance;
		this.purchaseType = purchaseType;
		this.limitCount = limitCount;
		this.ticketingStartTime = ticketingStartTime;
		this.ticketingCloseTime = ticketingCloseTime;
	}

	protected PerformanceDetail(Long id, Performance performance, PurchaseType purchaseType, int limitCount,
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
			throw new UnavailablePurchaseException("예매 가능 기간이 아닙니다.", NOT_PURCHASE_PERIOD, this.id);
		}
	}

	public void checkTicketLimit(final long purchasedCount) {
		if (purchasedCount >= limitCount) {
			throw new UnavailablePurchaseException("예매 가능 매수를 초과했습니다.", EXCEED_PURCHASE_LIMITS, this.id);
		}
	}
}
