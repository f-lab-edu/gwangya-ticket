package com.gwangya.performance.domain;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.gwangya.global.base.BaseEntity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "PERFORMANCE")
public class Performance extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "title", nullable = false)
	private String title;

	@Column(name = "genre", nullable = false)
	private String genre;

	@Column(name = "receiving_type", nullable = false)
	@Enumerated(EnumType.STRING)
	private ReceivingType receivingType;

	@Column(name = "performance_duration", nullable = false)
	private String duration;

	@Column(name = "location", nullable = false)
	private String location;

	@Column(name = "location_address", nullable = false)
	private String address;

	@OneToMany(mappedBy = "performance", cascade = CascadeType.PERSIST)
	private List<PerformanceDetail> performanceDetails = new ArrayList<>();

	public Performance(String title, String genre, ReceivingType receivingType, String duration, String location,
		String address, LocalDateTime createdAt, LocalDateTime updatedAt) {
		super(createdAt, updatedAt);
		this.title = title;
		this.genre = genre;
		this.receivingType = receivingType;
		this.duration = duration;
		this.location = location;
		this.address = address;
	}

	protected Performance(Long id, String title, String genre, ReceivingType receivingType, String duration,
		String location, String address, LocalDateTime createdAt, LocalDateTime updatedAt) {
		super(createdAt, updatedAt);
		this.id = id;
		this.title = title;
		this.genre = genre;
		this.receivingType = receivingType;
		this.duration = duration;
		this.location = location;
		this.address = address;
	}
}
