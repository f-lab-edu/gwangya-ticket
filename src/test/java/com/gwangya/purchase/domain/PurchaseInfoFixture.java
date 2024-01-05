package com.gwangya.purchase.domain;

import static com.gwangya.purchase.domain.ReceivingType.*;

import java.time.LocalDateTime;

import com.gwangya.performance.domain.PerformanceDetail;
import com.gwangya.purchase.domain.vo.PurchaseSerialNumber;
import com.gwangya.user.domain.User;

public class PurchaseInfoFixture {

	public static PurchaseInfo createPurchaseInfo(Long id, PerformanceDetail performanceDetail, User user) {
		return new PurchaseInfo(id, performanceDetail, user, ONSITE, new PurchaseSerialNumber("예매번호"),
			LocalDateTime.now(), LocalDateTime.now());
	}
}
