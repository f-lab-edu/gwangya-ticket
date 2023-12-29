package com.gwangya.performance.domain;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.gwangya.global.base.BaseEntity;
import com.gwangya.performance.domain.vo.SupportedReceivingType;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
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

	@Embedded
	private SupportedReceivingType receivingType;

	@Column(name = "performance_duration", nullable = false)
	private String duration;

	@Column(name = "location", nullable = false)
	private String location;

	@Column(name = "location_address", nullable = false)
	private String address;

	@OneToMany(mappedBy = "performance", cascade = CascadeType.PERSIST)
	private List<PerformanceDetail> performanceDetails = new ArrayList<>();

	public Performance(Long id, String title, String genre, SupportedReceivingType receivingType, String duration,
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

	public ReceivingType getReceivingType() {
		return receivingType.getType();
	}
}
