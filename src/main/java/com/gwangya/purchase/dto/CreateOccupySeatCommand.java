package com.gwangya.purchase.dto;

import java.util.List;

import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class CreateOccupySeatCommand {

	private Long userId;
	private List<Long> seatIds;
}
