package com.gwangya.purchase.dto;

import java.util.List;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class CreateSelectSeatCommand {

	private final List<Long> seatIds;
}
