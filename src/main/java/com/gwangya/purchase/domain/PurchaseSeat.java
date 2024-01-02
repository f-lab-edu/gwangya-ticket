package com.gwangya.purchase.domain;

import java.time.LocalDateTime;

import com.gwangya.global.base.BaseEntity;
import com.gwangya.performance.domain.Seat;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "PURCHASE_SEAT")
public class PurchaseSeat extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "purchase_info_id")
	private PurchaseInfo purchaseInfo;

	@OneToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "seat_id")
	private Seat seat;

	public PurchaseSeat(Long id, PurchaseInfo purchaseInfo, Seat seat, LocalDateTime createdAt,
		LocalDateTime updatedAt) {
		super(createdAt, updatedAt);
		this.id = id;
		this.purchaseInfo = purchaseInfo;
		this.seat = seat;
	}
}
