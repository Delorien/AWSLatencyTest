package com.payu.testator.AWSLatencyTest.S3;

import java.util.stream.Stream;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.services.s3.AmazonS3Client;
import com.payu.testator.AWSLatencyTest.AmazonWebServiceClientSetter;

public class S3ClientBuilder {

	public static AmazonS3Client build(AWSCredentials credentials,
			AmazonWebServiceClientSetter... amazonWebServiceClientSetters) {
		final AmazonS3Client amazonS3Client = new AmazonS3Client(credentials);

		Stream.of(amazonWebServiceClientSetters).forEach(s -> s.accept(amazonS3Client));
		return amazonS3Client;
	}
}
