package com.gwangya.global.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.hazelcast.config.Config;
import com.hazelcast.config.cp.CPSubsystemConfig;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;

@Configuration
public class HazelcastEmbeddedConfig {
	HazelcastInstance hazelcastInstance;
	HazelcastInstance hazelcastInstance2;
	HazelcastInstance hazelcastInstance3;

	@Bean
	public HazelcastInstance hazelcastInstance() {
		hazelcastInstance = Hazelcast.newHazelcastInstance(hazelcastConfig());
		hazelcastInstance2 = Hazelcast.newHazelcastInstance(hazelcastConfig());
		hazelcastInstance3 = Hazelcast.newHazelcastInstance(hazelcastConfig());
		return hazelcastInstance;
	}

	public Config hazelcastConfig() {
		Config config = new Config();
		config.setClusterName("gwangya");

		CPSubsystemConfig cpConfig = new CPSubsystemConfig();
		cpConfig.setGroupSize(3);
		cpConfig.setCPMemberCount(3);
		config.setCPSubsystemConfig(cpConfig);

		return config;
	}
}
