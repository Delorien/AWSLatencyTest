package com.payu.testator.AWSLatencyTest.SQS;

import static com.amazonaws.regions.Region.getRegion;
import static com.amazonaws.regions.Regions.US_EAST_1;
import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toList;

import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Stream;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.sqs.AmazonSQSClient;
import com.amazonaws.services.sqs.model.Message;
import com.amazonaws.services.sqs.model.ReceiveMessageRequest;
import com.amazonaws.services.sqs.model.SendMessageRequest;
import com.amazonaws.services.sqs.model.SendMessageResult;
import com.payu.testator.AWSLatencyTest.AWSTester;
import com.payu.testator.AWSLatencyTest.FileHelper;
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
						c -> executeTest(
								() -> sendOneTestMessage(
										SQSClientBuilder.build(c, s -> s.setRegion(getRegion(US_EAST_1)))),
								"write"));
	}

	public void runReceiveTest() {
		logger.info("===========================================");
		logger.info("Starting Amazon SQS Receive test");
		logger.info("Receiving messages from: {} ", QUEUEURL);
		logger.info("===========================================\n");

		credentials
				.ifPresent(
						c -> executeTest(
								() -> requestOneTestMessage(
										SQSClientBuilder.build(c, s -> s.setRegion(getRegion(US_EAST_1)))),
								"receive"));
	}

	private void executeTest(Supplier<LatencyTestResponse> supplier, String name) {

		try {
			List<LatencyTestResponse> results = Stream.generate(supplier).limit(AMOUNT).collect(toList());

			String testResponses = results.stream().map(Object::toString).collect(joining("\n"));
			testResponses += "\nAvarage = "
					+ results.stream().mapToDouble(r -> r.getDuration().toMillis()).average().orElse(0.0);

			logger.info(testResponses);
			FileHelper.write("SQS_test_" + name + "_", testResponses);

			logger.info("Receive tests successfully completed.");
		} catch (AmazonServiceException ase) {
			logger.error("Error Message:    " + ase.getMessage());
			logger.error("HTTP Status Code: " + ase.getStatusCode());
			logger.error("AWS Error Code:   " + ase.getErrorCode());
			logger.error("Error Type:       " + ase.getErrorType());
			logger.error("Request ID:       " + ase.getRequestId());
			logger.error("AmazonServiceException", ase);
		} catch (AmazonClientException ace) {
			logger.error("Error Message: " + ace.getMessage());
			logger.error("AmazonClientException", ace);
		}
	}

	private SQSResponseWrite sendOneTestMessage(AmazonSQSClient amazonSQSClient) {
		Instant start = Instant.now();
		SendMessageResult messageResult = amazonSQSClient
				.sendMessage(new SendMessageRequest(QUEUEURL, "Testing response delay."));
		Instant end = Instant.now();
		return new SQSResponseWrite(messageResult, Duration.between(start, end));
	}

	private SQSResponseReceive requestOneTestMessage(AmazonSQSClient amazonSQSClient) {
		Instant start = Instant.now();
		List<Message> messages = amazonSQSClient
				.receiveMessage(new ReceiveMessageRequest(QUEUEURL).withMaxNumberOfMessages(1)).getMessages();
		Instant end = Instant.now();
		Message resultMessage = messages.stream().findFirst().orElse(new Message());
		return new SQSResponseReceive(resultMessage, Duration.between(start, end));
	}
}
