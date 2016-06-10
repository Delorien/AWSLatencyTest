package com.payu.testator.ResponseTime;

import java.time.Duration;

import com.amazonaws.services.sqs.model.SendMessageResult;

public class SQSResponse {

	private SendMessageResult messageResult;
	private Duration duration;

	public SQSResponse(SendMessageResult messageResult, Duration duration) {
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
