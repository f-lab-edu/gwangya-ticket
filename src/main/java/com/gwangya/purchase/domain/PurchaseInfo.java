package com.gwangya.purchase.domain;

import com.gwangya.global.base.BaseEntity;
import com.gwangya.performance.domain.PerformanceDetail;
import com.gwangya.purchase.domain.vo.PurchaseSerialNumber;
import com.gwangya.user.domain.User;

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
import jakarta.persistence.Table;

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

	@Column(name = "receiving_type")
	@Enumerated(EnumType.STRING)
	private ReceivingType receivingType;

	@Embedded
	private PurchaseSerialNumber serialNumber;
}
