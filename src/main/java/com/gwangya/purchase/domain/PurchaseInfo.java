package com.gwangya.purchase.domain;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.gwangya.global.base.BaseEntity;
import com.gwangya.performance.domain.PerformanceDetail;
import com.gwangya.purchase.domain.vo.PurchaseSerialNumber;
import com.gwangya.user.domain.User;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "purchase_info")
public class PurchaseInfo extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "performance_detail_id")
	private PerformanceDetail performanceDetail;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id")
	private User user;

	@OneToMany(mappedBy = "purchaseInfo", cascade = CascadeType.PERSIST)
	private List<PurchaseSeat> purchaseSeats = new ArrayList<>();

	@Column(name = "receiving_type", nullable = false)
	@Enumerated(EnumType.STRING)
	private ReceivingType receivingType;

	@Embedded
	private PurchaseSerialNumber serialNumber;

	public PurchaseInfo(PerformanceDetail performanceDetail, User user, ReceivingType receivingType,
		PurchaseSerialNumber serialNumber, LocalDateTime createdAt, LocalDateTime updatedAt) {
		super(createdAt, updatedAt);
		this.performanceDetail = performanceDetail;
		this.user = user;
		this.receivingType = receivingType;
		this.serialNumber = serialNumber;
	}

	protected PurchaseInfo(Long id, PerformanceDetail performanceDetail, User user, ReceivingType receivingType,
		PurchaseSerialNumber serialNumber, LocalDateTime createdAt, LocalDateTime updatedAt) {
		super(createdAt, updatedAt);
		this.id = id;
		this.performanceDetail = performanceDetail;
		this.user = user;
		this.receivingType = receivingType;
		this.serialNumber = serialNumber;
	}

	public String getSerialNumber() {
		return serialNumber.getNumber();
	}
}
