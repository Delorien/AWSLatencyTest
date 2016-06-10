package com.payu.testator.AWSLatencyTest;

import java.time.Duration;

public abstract class SQSResponse {

	protected Duration duration;

	public SQSResponse(Duration duration) {
		this.duration = duration;
	}

	public Duration getDuration() {
		return duration;
	}

}
