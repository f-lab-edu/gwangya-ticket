package com.gwangya.performance.domain;

import com.gwangya.global.base.BaseEntity;
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
}
