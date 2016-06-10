package com.payu.testator.AWSLatencyTest;

import static com.amazonaws.regions.Region.getRegion;
import static com.amazonaws.regions.Regions.US_EAST_1;
import static com.payu.testator.AWSLatencyTest.PropertieKeys.QUEUE_AMOUNT_TEST;

import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.services.sqs.AmazonSQSClient;
import com.amazonaws.services.sqs.model.Message;
import com.amazonaws.services.sqs.model.ReceiveMessageRequest;
import com.amazonaws.services.sqs.model.SendMessageRequest;
import com.amazonaws.services.sqs.model.SendMessageResult;

public class SQSTester {

	private static final String QUEUEURL = PropertiesLoader.get(PropertieKeys.QUEUEURL.getKey());
	private static final String DEFAULT_AMOUNT = "5";
	private static final Long AMOUNT;

	static {
		AMOUNT = Long.valueOf(Optional.ofNullable(PropertiesLoader.get(QUEUE_AMOUNT_TEST.getKey()))
				.filter(s -> !s.isEmpty()).orElse(DEFAULT_AMOUNT));
	}

	private final Logger logger = LoggerFactory.getLogger(SQSTester.class);

	public void runWriteTest() {
		logger.info("===========================================");
		logger.info("Starting Amazon SQS Write test");
		logger.info("Sending a messages to Queue: {} ", QUEUEURL);
		logger.info("===========================================\n");

		Optional<AWSCredentials> credentials = new AWSHelper().getCredentials();
		credentials.ifPresent(c -> executeTest(
				() -> sendOneTestMessage(AmazonSQSClientBuilder.build(c, s -> s.setRegion(getRegion(US_EAST_1))))));
	}

	public void runReceiveTest() {
		logger.info("===========================================");
		logger.info("Starting Amazon SQS Receive test");
		logger.info("Receiving messages from: {} ", QUEUEURL);
		logger.info("===========================================\n");

		Optional<AWSCredentials> credentials = new AWSHelper().getCredentials();
		credentials.ifPresent(c -> executeTest(
				() -> requestOneTestMessage(AmazonSQSClientBuilder.build(c, s -> s.setRegion(getRegion(US_EAST_1))))));
	}

	private void executeTest(Supplier<SQSResponse> supplier) {

		try {
			List<SQSResponse> results = Stream.generate(supplier).limit(AMOUNT).collect(Collectors.toList());

			String testResponses = results.stream().map(Object::toString).collect(Collectors.joining("\n"));
			testResponses += "\nAvarage = "
					+ results.stream().mapToDouble(r -> r.getDuration().toMillis()).average().orElse(0.0);

			logger.info(testResponses);
			FileHelper.write("SQS_test_", testResponses);

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
