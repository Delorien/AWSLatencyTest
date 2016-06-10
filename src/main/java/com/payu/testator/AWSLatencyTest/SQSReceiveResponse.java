package com.payu.testator.AWSLatencyTest;

import java.time.Duration;

import com.amazonaws.services.sqs.model.Message;

public class SQSReceiveResponse {

	private Message message;
	private Duration duration;

	public SQSReceiveResponse(Message message, Duration duration) {
		this.message = message;
		this.duration = duration;
	}

	public Duration getDuration() {
		return duration;
	}

	public Message getMessage() {
		return message;
	}

	@Override
	public String toString() {
		return "Message Id: " + message.getMessageId() + " - Duration on milliseconds : " + duration.toMillis();
	}
}
