package com.payu.testator.AWSLatencyTest;

import java.time.Duration;

import com.amazonaws.services.sqs.model.SendMessageResult;

public class SQSResponseWrite extends SQSResponse {

	private SendMessageResult messageResult;

	public SQSResponseWrite(SendMessageResult messageResult, Duration duration) {
		super(duration);
		this.messageResult = messageResult;

	}

	public SendMessageResult getMessageResult() {
		return messageResult;
	}

	@Override
	public String toString() {
		return "Message Id: " + messageResult.getMessageId() + " - Duration on milliseconds : " + duration.toMillis();
	}
}
