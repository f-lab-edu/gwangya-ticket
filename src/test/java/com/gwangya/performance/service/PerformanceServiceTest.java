package com.gwangya.performance.service;

import static com.gwangya.performance.domain.PerformanceDetailFixture.*;
import static com.gwangya.performance.domain.PerformanceFixture.*;
import static com.gwangya.performance.domain.SeatFixture.*;
import static com.gwangya.purchase.domain.PurchaseInfoFixture.*;
import static com.gwangya.purchase.domain.PurchaseSeatFixture.*;
import static com.gwangya.user.domain.UserFixture.*;
import static org.assertj.core.api.Assertions.*;

import java.time.LocalDateTime;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.gwangya.global.exception.EntityNotFoundException;
import com.gwangya.performance.domain.Performance;
import com.gwangya.performance.domain.PerformanceDetail;
import com.gwangya.performance.domain.Seat;
import com.gwangya.performance.exception.UnavailablePurchaseException;
import com.gwangya.performance.repository.InMemoryPerformanceRepository;
import com.gwangya.performance.repository.InMemorySeatRepository;
import com.gwangya.purchase.domain.PurchaseInfo;
import com.gwangya.purchase.domain.PurchaseSeat;
import com.gwangya.purchase.repository.InMemoryPurchaseRepository;
import com.gwangya.user.domain.User;
import com.gwangya.user.repository.InMemoryUserRepository;
import com.gwangya.user.repository.UserRepository;

class PerformanceServiceTest {

	InMemoryPerformanceRepository performanceRepository;

	InMemoryPurchaseRepository purchaseRepository;

	InMemorySeatRepository seatRepository;

	UserRepository userRepository;

	PerformanceService performanceService;

	Performance performance;

	PerformanceDetail performanceDetail;

	User user;

	Seat seat;

	@BeforeEach
	void setUp() {
		performanceRepository = new InMemoryPerformanceRepository();
		purchaseRepository = new InMemoryPurchaseRepository();
		performanceService = new PerformanceService(performanceRepository, purchaseRepository);
		seatRepository = new InMemorySeatRepository();
		userRepository = new InMemoryUserRepository();

		performance = performanceRepository.save(createPerformance(1L));
		performanceDetail = performanceRepository.save(
			createPerformanceDetail(1L, performance, LocalDateTime.now().minusDays(1),
				LocalDateTime.now().plusDays(30))
		);
		performance.getPerformanceDetails().add(performanceDetail);
		seat = seatRepository.save(createSeat(1L, performanceDetail));

		user = userRepository.save(createUser(1L));

	}

	@DisplayName("존재하지 않는 공연이면 예외가 발생한다.")
	@Test
	void if_concert_is_not_existed_then_exception_is_thrown() {
		// given
		final long notExistPerformanceDetailId = 2L;

		// when & then
		assertThatThrownBy(
			() -> performanceService.searchPurchasablePerformanceDetailById(notExistPerformanceDetailId, user.getId()))
			.isInstanceOf(EntityNotFoundException.class)
			.hasMessageContaining("존재하지 않는 공연입니다.");
	}

	@DisplayName("예매 가능 기간이 아닐 경우 에외가 발생한다.")
	@Test
	void if_it_is_outside_of_the_availability_period_then_exception_is_thrown() {
		// given
		LocalDateTime ticketingStartTimeIsFuture = LocalDateTime.now().plusDays(1);
		PerformanceDetail detail = createPerformanceDetail(2L, performance, ticketingStartTimeIsFuture,
			ticketingStartTimeIsFuture.plusDays(30));
		performance.getPerformanceDetails().add(detail);
		performanceRepository.save(detail);

		// when & then
		assertThatThrownBy(
			() -> performanceService.searchPurchasablePerformanceDetailById(detail.getId(), user.getId()))
			.isInstanceOf(UnavailablePurchaseException.class)
			.hasMessageContaining("예매 가능 기간이 아닙니다.");
	}

	@DisplayName("예매 가능 매수를 초과할 경우 예외가 발생한다.")
	@Test
	void if_the_number_of_reserves_is_exceeded_then_exception_is_thrown() {
		// given
		PurchaseInfo purchaseInfo = purchaseRepository.save(createPurchaseInfo(1L, performanceDetail, user));
		PurchaseSeat purchaseSeat = purchaseRepository.save(createPurchaseSeat(1L, purchaseInfo, seat));
		purchaseInfo.getPurchaseSeats().add(purchaseSeat);

		// when & then
		assertThatThrownBy(
			() -> performanceService.searchPurchasablePerformanceDetailById(performanceDetail.getId(), user.getId()))
			.isInstanceOf(UnavailablePurchaseException.class)
			.hasMessageContaining("예매 가능 매수를 초과했습니다.");
	}

}