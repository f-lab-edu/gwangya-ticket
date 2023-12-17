package com.gwangya.performance.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gwangya.global.exception.NoExistEntityException;
import com.gwangya.global.util.ConvertUtil;
import com.gwangya.performance.domain.PerformanceDetail;
import com.gwangya.performance.domain.Seat;
import com.gwangya.performance.dto.SeatDto;
import com.gwangya.performance.repository.PerformanceRepository;
import com.gwangya.performance.repository.SeatRepository;
import com.gwangya.purchase.repository.PurchaseRepository;
import com.gwangya.user.domain.User;
import com.gwangya.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class SeatService {

	private final PerformanceRepository performanceRepository;

	private final SeatRepository seatRepository;

	private final PurchaseRepository purchaseRepository;

	private final UserRepository userRepository;

	@Transactional(readOnly = true)
	public List<SeatDto> searchAllRemainingSeats(final Long detailId, final Long userId) {
		User user = userRepository.findById(userId)
			.orElseThrow(() -> new NoExistEntityException("존재하지 않는 유저입니다."));

		PerformanceDetail performanceDetail = performanceRepository.findPerformanceDetailById(detailId)
			.orElseThrow(() -> new NoExistEntityException("존재하지 않는 공연입니다."));

		performanceDetail.checkPurchasePeriod(LocalDateTime.now());
		performanceDetail.checkTicketLimit(purchaseRepository, user);

		List<Seat> result = seatRepository.findRemainingAllByPerformanceDetail(
			performanceDetail);

		return ConvertUtil.convertList(result, SeatDto.class);
	}
}
