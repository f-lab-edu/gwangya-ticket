package com.gwangya.performance.domain;

import com.gwangya.global.base.BaseEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Entity
@Table(name = "SEAT")
public class Seat extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "performance_detail_id")
	private PerformanceDetail performanceDetail;

	@Column(name = "class", nullable = false)
	private String seatClass;

	@Column(name = "floor", nullable = false)
	private String floor;

	@Column(name = "zone", nullable = false)
	private String zone;

	@Column(name = "seat_number", nullable = false)
	private String number;

	@Column(name = "cost", nullable = false)
	private int cost;
}
