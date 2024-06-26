package com.gwangya.performance.service;

import static com.gwangya.lock.HazelcastLockService.*;
import static com.gwangya.performance.exception.UnavailablePurchaseType.*;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gwangya.lock.LockService;
import com.gwangya.lock.exception.LockOccupationException;
import com.gwangya.performance.domain.Seat;
import com.gwangya.performance.dto.OccupancyInfoCondition;
import com.gwangya.performance.dto.SeatDto;
import com.gwangya.performance.dto.SeatOccupancyInfo;
import com.gwangya.performance.exception.UnavailablePurchaseException;
import com.gwangya.performance.repository.SeatRepository;
import com.gwangya.purchase.domain.LockInfo;
import com.gwangya.purchase.dto.OccupySeatDto;
import com.gwangya.purchase.dto.OccupySeatInfo;
import com.hazelcast.cp.lock.FencedLock;
import com.hazelcast.internal.util.ThreadUtil;
import com.hazelcast.map.IMap;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class SeatService {

	private final SeatRepository seatRepository;

	private final LockService lockService;

	@Transactional(readOnly = true)
	public List<SeatDto> searchAllRemainingSeats(final long detailId) {
		List<Seat> result = seatRepository.findRemainingAllByPerformanceDetailId(detailId);
		return result.stream()
			.map(seat -> new SeatDto(
					seat.getId(),
					seat.getSeatClass(),
					seat.getFloor(),
					seat.getZone(),
					seat.getNumber(),
					seat.getCost()
				)
			)
			.toList();
	}

	/**
	 * 선택한 좌석에 대한 점유를 설정하는 메서드
	 * 선택한 좌석({@link OccupySeatInfo#getSeatIds()})에 대해 {@link FencedLock}획득이 모두 성공하면,
	 * {@link IMap}에 저장됩니다.(5분 간 유효)
	 * <li>occupiedSeats.key : 좌석(Seat.id)</li>
	 * <li>occupiedSeats.value : LockInfo(유저 PK, Thread id)</li>
	 * Lock 획득이 하나라도 실패하면 획득한 Lock을 모두 반납하고 {@link UnavailablePurchaseException}을 던집니다.
	 *
	 * @param occupySeatInfo
	 */
	public OccupySeatDto occupySeats(final OccupySeatInfo occupySeatInfo) {
		final List<Long> occupySeats = new ArrayList<>();
		final IMap<Long, LockInfo> occupiedSeats = (IMap<Long, LockInfo>)lockService.getLockMap(SEAT_SESSION_MAP_NAME);
		try {
			final List<FencedLock> validLocks = lockService.tryLockAll(3, TimeUnit.MILLISECONDS,
				occupySeatInfo.getSeatIds().stream()
					.map(seatId -> String.valueOf(seatId))
					.toList(),
				occupiedSeats
			);
			if (!isSuccessfullyOccupied(occupySeatInfo.getSeatIds(), validLocks)) {
				lockService.unlockAll(validLocks);
				throw new UnavailablePurchaseException("이미 선택된 좌석입니다.", SELECTED_SEAT,
					occupySeatInfo.getPerformanceDetailId());
			}
			validLocks.forEach(lock -> {
					final Long seatId = Long.valueOf(lock.getName());
					occupiedSeats.put(seatId,
						new LockInfo(occupySeatInfo.getUserId(), ThreadUtil.getThreadId()), 5,
						TimeUnit.MINUTES);
					occupySeats.add(seatId);
				}
			);
			return new OccupySeatDto(occupySeats);
		} catch (LockOccupationException e) {
			throw new UnavailablePurchaseException("이미 선택된 좌석입니다.", SELECTED_SEAT,
				occupySeatInfo.getPerformanceDetailId(), Long.valueOf(e.getName()));
		}
	}

	private boolean isSuccessfullyOccupied(final List<Long> selectSeats, final List<FencedLock> validLocks) {
		final List<Long> lockNames = validLocks.stream()
			.map(lock -> Long.valueOf(lock.getName()))
			.toList();
		return lockNames.containsAll(selectSeats);
	}

	@Transactional(readOnly = true)
	public SeatOccupancyInfo searchSeatOccupancyInfo(final OccupancyInfoCondition occupancyInfoCondition) {
		final IMap<Long, LockInfo> occupiedSeats = (IMap<Long, LockInfo>)lockService.getLockMap(SEAT_SESSION_MAP_NAME);
		final List<Long> occupancySeats = occupiedSeats.entrySet().stream()
			.filter(occupancyInfo -> occupancyInfo.getValue().getUserId() == occupancyInfoCondition.getUserId())
			.map(Map.Entry::getKey)
			.toList();
		final long totalSeatCount = seatRepository.countAllByPerformanceDetailId(
			occupancyInfoCondition.getPerformanceDetailId());
		return new SeatOccupancyInfo(occupancyInfoCondition.getUserId(), occupancySeats,
			calculateOccupancyRate(occupancySeats.size(), totalSeatCount));
	}

	private double calculateOccupancyRate(final long occupancySeatCount, final long totalSeatCount) {
		return BigDecimal.valueOf(occupancySeatCount)
			.divide(BigDecimal.valueOf(totalSeatCount), 2, RoundingMode.HALF_UP)
			.doubleValue();
	}
}
