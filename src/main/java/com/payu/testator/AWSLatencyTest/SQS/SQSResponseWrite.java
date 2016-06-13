package com.payu.testator.AWSLatencyTest.SQS;

import java.time.Duration;

import com.amazonaws.services.sqs.model.SendMessageResult;
import com.payu.testator.AWSLatencyTest.LatencyTestResponse;

public class SQSResponseWrite extends LatencyTestResponse {

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
