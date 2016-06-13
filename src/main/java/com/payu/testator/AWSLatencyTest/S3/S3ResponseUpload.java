package com.payu.testator.AWSLatencyTest.S3;

import java.time.Duration;

import com.amazonaws.services.s3.model.PutObjectResult;
import com.payu.testator.AWSLatencyTest.LatencyTestResponse;

public class S3ResponseUpload extends LatencyTestResponse {

	private PutObjectResult putObjectResult;

	public S3ResponseUpload(PutObjectResult putObjectResult, Duration duration) {
		super(duration);
		this.putObjectResult = putObjectResult;
	}

	public PutObjectResult getPutObjectResult() {
		return putObjectResult;
	}

	@Override
	public String toString() {
		return "ETag : " + putObjectResult.getETag() + " - Duration on milliseconds : " + duration.toMillis();
	}
}
