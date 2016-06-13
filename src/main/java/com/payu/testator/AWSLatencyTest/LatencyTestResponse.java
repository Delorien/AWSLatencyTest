package com.payu.testator.AWSLatencyTest;

import java.time.Duration;

public class LatencyTestResponse {

	private String key;
	private Duration duration;

	public LatencyTestResponse(String key, Duration duration) {
		this.key = key;
		this.duration = duration;
	}

	public Duration getDuration() {
		return duration;
	}

	public String getKey() {
		return key;
	}

	@Override
	public String toString() {
		return "Used : " + key + " - with Duration on milliseconds : " + duration.toMillis();
	}

}
