package com.gwangya.global.testConfig;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

import com.hazelcast.client.HazelcastClient;
import com.hazelcast.client.config.ClientConfig;
import com.hazelcast.client.config.ClientNetworkConfig;
import com.hazelcast.core.HazelcastInstance;

@TestConfiguration
public class HazelcastTestConfig {

	@Bean
	public HazelcastInstance hazelcastInstance() {
		return HazelcastClient.newHazelcastClient(hazelcastConfig());
	}

	public ClientConfig hazelcastConfig() {
		ClientConfig config = new ClientConfig();
		config.setClusterName("gwangya-1");

		ClientNetworkConfig networkConfig = new ClientNetworkConfig();
		config.setProperty("hazelcast.clientengine.thread.count", "100");
		networkConfig.addAddress("223.130.131.129:5701");
		networkConfig.addAddress("223.130.131.129:5702");
		networkConfig.addAddress("223.130.131.129:5703");

		config.setNetworkConfig(networkConfig);
		return config;
	}
}
