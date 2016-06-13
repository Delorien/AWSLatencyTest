package com.payu.testator.AWSLatencyTest.SQS;

import java.util.function.Consumer;
import java.util.stream.Stream;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.services.sqs.AmazonSQSClient;

public class AmazonSQSClientBuilder {

	@FunctionalInterface
	public interface AmazonSQSClientSetter extends Consumer<AmazonSQSClient> {
	};

	public static AmazonSQSClient build(AWSCredentials credentials, AmazonSQSClientSetter... amazonSQSClientSetters) {
		final AmazonSQSClient amazonSQSClient = new AmazonSQSClient(credentials);

		Stream.of(amazonSQSClientSetters).forEach(s -> s.accept(amazonSQSClient));
		return amazonSQSClient;
	}
}
