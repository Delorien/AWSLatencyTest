package com.payu.testator.AWSLatencyTest.SQS;

import java.time.Duration;

import com.amazonaws.services.sqs.model.Message;

public class SQSResponseReceive extends SQSResponse {

	private Message message;

	public SQSResponseReceive(Message message, Duration duration) {
		super(duration);
		this.message = message;
	}

	public Message getMessage() {
		return message;
	}

	@Override
	public String toString() {
		return "Message Id: " + message.getMessageId() + " - Duration on milliseconds : " + duration.toMillis();
	}
}
