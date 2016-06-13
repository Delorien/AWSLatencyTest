package com.payu.testator.AWSLatencyTest.SQS;

import java.util.stream.Stream;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.services.sqs.AmazonSQSClient;
import com.payu.testator.AWSLatencyTest.AmazonWebServiceClientSetter;

public class SQSClientBuilder {

	public static AmazonSQSClient build(AWSCredentials credentials, AmazonWebServiceClientSetter... amazonWebServiceClientSetters) {
		final AmazonSQSClient amazonSQSClient = new AmazonSQSClient(credentials);

		Stream.of(amazonWebServiceClientSetters).forEach(s -> s.accept(amazonSQSClient));
		return amazonSQSClient;
	}
}
