package com.payu.testator.AWSLatencyTest.Dynamo;

import java.util.stream.Stream;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.payu.testator.AWSLatencyTest.AmazonWebServiceClientSetter;

public class DynamoDBClientBuilder {

	public static AmazonDynamoDBClient build(AWSCredentials credentials,
			AmazonWebServiceClientSetter... amazonWebServiceClientSetters) {
		final AmazonDynamoDBClient amazonDynamoDBClient = new AmazonDynamoDBClient(credentials);

		Stream.of(amazonWebServiceClientSetters).forEach(s -> s.accept(amazonDynamoDBClient));
		return amazonDynamoDBClient;
	}
}
