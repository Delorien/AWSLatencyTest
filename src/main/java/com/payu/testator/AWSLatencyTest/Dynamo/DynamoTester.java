package com.payu.testator.AWSLatencyTest.Dynamo;

import static com.amazonaws.regions.Region.getRegion;
import static com.amazonaws.regions.Regions.US_EAST_1;
import static java.util.stream.Collectors.toList;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.GetItemResult;
import com.payu.testator.AWSLatencyTest.AWSHelper;
import com.payu.testator.AWSLatencyTest.AWSTester;
import com.payu.testator.AWSLatencyTest.LatencyTestResponse;
import com.payu.testator.AWSLatencyTest.PropertieKeys;
import com.payu.testator.AWSLatencyTest.PropertiesLoader;

public class DynamoTester extends AWSTester {

	private static final String TABLE = PropertiesLoader.get(PropertieKeys.TABLE.getKey());
	private List<Map<String, AttributeValue>> elements = new ArrayList<>();

	public void runPutTest() {
		logger.info("===========================================");
		logger.info("Starting Amazon Dynamo Put test");
		logger.info("Put elements in Table: {}", TABLE);
		logger.info("===========================================\n");

		Optional<AWSCredentials> credentials = new AWSHelper().getCredentials();
		credentials
				.ifPresent(
						c -> simpleTest(
								() -> putOneElement(
										DynamoDBClientBuilder.build(c, s -> s.setRegion(getRegion(US_EAST_1)))),
								"Dynamo_test_put_"));
	}

	public void runGetTest() {
		logger.info("===========================================");
		logger.info("Starting Amazon Dynamo Get test");
		logger.info("Get elements from Table: {}", TABLE);
		logger.info("===========================================\n");

		Optional<AWSCredentials> credentials = new AWSHelper().getCredentials();
		credentials.ifPresent(c -> simpleTestList(() -> elements.stream().map(e -> getOneElement(

				DynamoDBClientBuilder.build(c, s -> s.setRegion(getRegion(US_EAST_1)))

				, e)).collect(toList()), "Dynamo_test_get_")

		);
	}

	private LatencyTestResponse putOneElement(AmazonDynamoDBClient amazonDynamoDBClient) {
		Instant start = Instant.now();
		Map<String, AttributeValue> element = generateRandomElement();
		amazonDynamoDBClient.putItem(TABLE, element);
		Instant end = Instant.now();
		elements.add(element);
		return new LatencyTestResponse(element.get("id").toString(), Duration.between(start, end));
	}

	private LatencyTestResponse getOneElement(AmazonDynamoDBClient amazonDynamoDBClient,
			Map<String, AttributeValue> element) {
		Instant start = Instant.now();
		GetItemResult getItemResult = amazonDynamoDBClient.getItem(TABLE, element);
		Instant end = Instant.now();
		return new LatencyTestResponse(getItemResult.getItem().get("id").toString(), Duration.between(start, end));
	}

	private Map<String, AttributeValue> generateRandomElement() {
		Map<String, AttributeValue> element = new HashMap<>();
		element.put("id", new AttributeValue(UUID.randomUUID().toString()));
		return element;
	}
}
