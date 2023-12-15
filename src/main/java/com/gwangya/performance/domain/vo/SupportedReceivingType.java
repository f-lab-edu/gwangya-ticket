package com.gwangya.performance.domain.vo;

import com.gwangya.performance.domain.ReceivingType;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class SupportedReceivingType {

	@Column(name = "receiving_type")
	@Enumerated(EnumType.STRING)
	private ReceivingType type;

	public static SupportedReceivingType of(ReceivingType receivingType) {
		return new SupportedReceivingType(receivingType);
	}
}
