package com.gwangya.performance.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.gwangya.global.testConfig.HazelcastTestConfig;
import com.gwangya.global.testConfig.JpaTestConfig;
import com.gwangya.lock.HazelcastLockService;
import com.gwangya.performance.repository.SeatRepository;
import com.gwangya.performance.repository.impl.SeatRepositoryImpl;
import com.hazelcast.core.HazelcastInstance;

@SpringBootTest(classes = {HazelcastTestConfig.class, JpaTestConfig.class, HazelcastLockService.class,
	SeatRepositoryImpl.class, SeatService.class})
public class LockServiceIntegrationTest {

	@Autowired
	private HazelcastInstance hazelcastInstance;

	@Autowired
	private HazelcastLockService hazelcastLockService;

	@Autowired
	private SeatRepository seatRepository;

	@Autowired
	private SeatService seatService;

	@Test
	void contextLoads() {

	}
}
