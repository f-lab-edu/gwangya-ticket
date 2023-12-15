package com.gwangya.purchase.domain;

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
import lombok.Getter;

@Entity
@Table(name = "PURCHASE_SEAT")
@Getter
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
}
