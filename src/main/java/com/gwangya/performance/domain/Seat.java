package com.gwangya.performance.domain;

import java.time.LocalDateTime;

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
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
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

	public Seat(PerformanceDetail performanceDetail, String seatClass, String floor, String zone, String number,
		int cost, LocalDateTime createdAt, LocalDateTime updatedAt) {
		super(createdAt, updatedAt);
		this.performanceDetail = performanceDetail;
		this.seatClass = seatClass;
		this.floor = floor;
		this.zone = zone;
		this.number = number;
		this.cost = cost;
	}

	protected Seat(Long id, PerformanceDetail performanceDetail, String seatClass, String floor, String zone,
		String number, int cost, LocalDateTime createdAt, LocalDateTime updatedAt) {
		super(createdAt, updatedAt);
		this.id = id;
		this.performanceDetail = performanceDetail;
		this.seatClass = seatClass;
		this.floor = floor;
		this.zone = zone;
		this.number = number;
		this.cost = cost;
	}
}
