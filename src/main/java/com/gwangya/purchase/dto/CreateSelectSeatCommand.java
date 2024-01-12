package com.gwangya.purchase.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class CreateSelectSeatCommand {

	private final long userId;

	private final long seatId;
}
