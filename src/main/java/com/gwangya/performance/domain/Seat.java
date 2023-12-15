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

@Entity
@Table(name = "SEAT")
public class Seat extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "performance_detail_id")
	private PerformanceDetail detail;

	@Column(name = "class")
	private String seatClass;

	@Column(name = "floor")
	private String floor;

	@Column(name = "zone")
	private String zone;

	@Column(name = "seat_number")
	private String number;

	@Column(name = "cost")
	private Integer cost;
}
