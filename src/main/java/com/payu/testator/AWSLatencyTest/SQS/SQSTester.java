package com.payu.testator.AWSLatencyTest.SQS;

import static com.amazonaws.regions.Region.getRegion;
import static com.amazonaws.regions.Regions.US_EAST_1;

import java.time.Duration;
import java.time.Instant;
import java.util.List;

import com.amazonaws.services.sqs.AmazonSQSClient;
import com.amazonaws.services.sqs.model.Message;
import com.amazonaws.services.sqs.model.ReceiveMessageRequest;
import com.amazonaws.services.sqs.model.SendMessageRequest;
import com.amazonaws.services.sqs.model.SendMessageResult;
import com.payu.testator.AWSLatencyTest.AWSTester;
import com.payu.testator.AWSLatencyTest.LatencyTestResponse;
import com.payu.testator.AWSLatencyTest.PropertieKeys;
import com.payu.testator.AWSLatencyTest.PropertiesLoader;

public class SQSTester extends AWSTester {

	private static final String QUEUEURL = PropertiesLoader.get(PropertieKeys.QUEUEURL.getKey());

	public void runWriteTest() {
		logger.info("===========================================");
		logger.info("Starting Amazon SQS Write test");
		logger.info("Sending a messages to Queue: {} ", QUEUEURL);
		logger.info("===========================================\n");

		credentials
				.ifPresent(
						c -> simpleTest(
								() -> sendOneTestMessage(
										SQSClientBuilder.build(c, s -> s.setRegion(getRegion(US_EAST_1)))),
								"SQS_test_write_"));
	}

	public void runReceiveTest() {
		logger.info("===========================================");
		logger.info("Starting Amazon SQS Receive test");
		logger.info("Receiving messages from: {} ", QUEUEURL);
		logger.info("===========================================\n");

		credentials
				.ifPresent(
						c -> simpleTest(
								() -> requestOneTestMessage(
										SQSClientBuilder.build(c, s -> s.setRegion(getRegion(US_EAST_1)))),
								"SQS_test_receive_"));
	}

	private LatencyTestResponse sendOneTestMessage(AmazonSQSClient amazonSQSClient) {
		Instant start = Instant.now();
		SendMessageResult messageResult = amazonSQSClient
				.sendMessage(new SendMessageRequest(QUEUEURL, "Testing response delay."));
		Instant end = Instant.now();
		return new LatencyTestResponse(messageResult.getMessageId(), Duration.between(start, end));
	}

	private LatencyTestResponse requestOneTestMessage(AmazonSQSClient amazonSQSClient) {
		Instant start = Instant.now();
		List<Message> messages = amazonSQSClient
				.receiveMessage(new ReceiveMessageRequest(QUEUEURL).withMaxNumberOfMessages(1)).getMessages();
		Instant end = Instant.now();
		Message resultMessage = messages.stream().findFirst().orElse(new Message());
		return new LatencyTestResponse(resultMessage.getMessageId(), Duration.between(start, end));
	}
}
