package com.gwangya.performance.domain;

import com.gwangya.global.base.BaseEntity;
import com.gwangya.performance.domain.vo.SupportedReceivingType;

import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "PERFORMANCE")
public class Performance extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "title")
	private String title;

	@Column(name = "genre")
	private String genre;

	@Embedded
	private SupportedReceivingType receivingType;

	@Column(name = "performance_duration")
	private String duration;

	@Column(name = "location")
	private String location;

	@Column(name = "location_address")
	private String address;
}
