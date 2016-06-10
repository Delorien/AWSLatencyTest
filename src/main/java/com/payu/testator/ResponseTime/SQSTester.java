package com.payu.testator.ResponseTime;

import static com.amazonaws.regions.Region.getRegion;
import static com.amazonaws.regions.Regions.US_EAST_1;

import java.time.Duration;
import java.time.Instant;
import java.util.List;
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

	private static final String QUEUEURL = PropertiesLoader.get(PropertieKeys.QUEUEURL.getKey());
	private static final String DEFAULT_AMOUNT = "5";

	private final Logger logger = LoggerFactory.getLogger(SQSTester.class);

	public void run() {
		Optional<AWSCredentials> credentials = new AWSHelper().getCredentials();
		credentials.ifPresent(c -> writeTest(AmazonSQSClientBuilder.build(c, s -> s.setRegion(getRegion(US_EAST_1)))));
	}

	private void writeTest(AmazonSQSClient amazonSQSClient) {

		logger.info("===========================================");
		logger.info("Starting Amazon SQS test");
		logger.info("Sending a messages to Queue: {} ", QUEUEURL);
		logger.info("===========================================\n");

		try {

			String amount = Optional.ofNullable(PropertiesLoader.get(PropertieKeys.QUEUE_AMOUNT_TEST.getKey()))
					.filter(s -> !s.isEmpty()).orElse(DEFAULT_AMOUNT);

			List<SQSResponse> results = Stream.generate(() -> sendOneTestMessage(amazonSQSClient))
					.limit(Long.valueOf(amount)).collect(Collectors.toList());

			String testResponses = results.stream().map(Object::toString).collect(Collectors.joining("\n"));
			testResponses += "\nAvarage = "
					+ results.stream().mapToDouble(r -> r.getDuration().toMillis()).average().orElse(0.0);

			logger.info(testResponses);
			FileHelper.write(testResponses);

			logger.info("writing tests successfully completed.");
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

	private SQSResponse sendOneTestMessage(AmazonSQSClient amazonSQSClient) {
		Instant start = Instant.now();
		SendMessageResult messageResult = amazonSQSClient
				.sendMessage(new SendMessageRequest(QUEUEURL, "Testing response delay."));
		Instant end = Instant.now();
		return new SQSResponse(messageResult, Duration.between(start, end));
	}
}
