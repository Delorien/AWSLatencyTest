package com.payu.testator.AWSLatencyTest;

import java.time.Duration;

import com.amazonaws.services.sqs.model.SendMessageResult;

public class SQSWriteResponse {

	private SendMessageResult messageResult;
	private Duration duration;

	public SQSWriteResponse(SendMessageResult messageResult, Duration duration) {
		this.messageResult = messageResult;
		this.duration = duration;
	}

	public Duration getDuration() {
		return duration;
	}

	public SendMessageResult getMessageResult() {
		return messageResult;
	}

	@Override
	public String toString() {
		return "Message Id: " + messageResult.getMessageId() + " - Duration on milliseconds : " + duration.toMillis();
	}
}
