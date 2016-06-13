package com.payu.testator.AWSLatencyTest.S3;

import java.time.Duration;

import com.amazonaws.services.s3.model.S3Object;
import com.payu.testator.AWSLatencyTest.LatencyTestResponse;

public class S3ResponseDownload extends LatencyTestResponse {

	private S3Object s3Object;

	public S3ResponseDownload(S3Object s3Object, Duration duration) {
		super(duration);
		this.s3Object = s3Object;
	}

	public S3Object getS3Object() {
		return s3Object;
	}

	@Override
	public String toString() {
		return "Object Key : " + s3Object.getKey() + " - Duration on milliseconds : " + duration.toMillis();
	}
}
