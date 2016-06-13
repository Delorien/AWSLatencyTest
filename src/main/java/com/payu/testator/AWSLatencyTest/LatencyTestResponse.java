package com.payu.testator.AWSLatencyTest;

import java.time.Duration;

public abstract class LatencyTestResponse {

	protected Duration duration;

	public LatencyTestResponse(Duration duration) {
		this.duration = duration;
	}

	public Duration getDuration() {
		return duration;
	}

}
