package com.payu.testator.ResponseTime;

import static com.amazonaws.regions.Region.getRegion;
import static com.amazonaws.regions.Regions.US_EAST_1;

import java.time.Duration;
import java.time.Instant;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.services.sqs.AmazonSQSClient;
import com.amazonaws.services.sqs.model.SendMessageRequest;
import com.amazonaws.services.sqs.model.SendMessageResult;

public class SQSTester {

	private static final String QUEUEURL = "https://sqs.us-east-1.amazonaws.com/817345971784/DEV-rackspace-test";

	private final Logger logger = LoggerFactory.getLogger(SQSTester.class);

	private int amountOfTests;

	public void run(int amountOfTests) {
		this.amountOfTests = amountOfTests;

		Optional<AWSCredentials> credentials = new AWSHelper().getCredentials();

		credentials.ifPresent(c -> writeTest(AmazonSQSClientBuilder.build(c, s -> s.setRegion(getRegion(US_EAST_1)))));

	}

	private void writeTest(AmazonSQSClient amazonSQSClient) {

		logger.info("===========================================");
		logger.info("Starting Amazon SQS test");
		logger.info("Sending a messages to Queue: {} ", QUEUEURL);
		logger.info("===========================================\n");

		try {
			String testResponses = Stream.generate(() -> sendOneTestMessage(amazonSQSClient)).limit(amountOfTests)
					.collect(Collectors.joining("\n"));
			logger.info(testResponses);
			FileHelper.write(testResponses);

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

	private String sendOneTestMessage(AmazonSQSClient amazonSQSClient) {
		Instant start = Instant.now();
		SendMessageResult sendMessage = amazonSQSClient
				.sendMessage(new SendMessageRequest(QUEUEURL, "Testing response delay."));
		Instant end = Instant.now();
		Duration duration = Duration.between(start, end);
		return "Message Id: " + sendMessage.getMessageId() + " - Duration on milliseconds : " + duration.toMillis();
	}
}
