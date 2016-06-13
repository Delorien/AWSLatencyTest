package com.payu.testator.AWSLatencyTest.S3;

import static com.amazonaws.regions.Region.getRegion;
import static com.amazonaws.regions.Regions.US_EAST_1;
import static java.util.stream.Collectors.toList;

import java.io.File;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.PutObjectResult;
import com.amazonaws.services.s3.model.S3Object;
import com.payu.testator.AWSLatencyTest.AWSHelper;
import com.payu.testator.AWSLatencyTest.AWSTester;
import com.payu.testator.AWSLatencyTest.FileHelper;
import com.payu.testator.AWSLatencyTest.LatencyTestResponse;
import com.payu.testator.AWSLatencyTest.PropertieKeys;
import com.payu.testator.AWSLatencyTest.PropertiesLoader;

public class S3Tester extends AWSTester {

	private static final String BUCKET_NAME = PropertiesLoader.get(PropertieKeys.BUCKET.getKey());
	private List<File> files = new ArrayList<>();

	public void runUpload() {

		logger.info("===========================================");
		logger.info("Starting Amazon S3 Upload test");
		logger.info("Upload Files to Bucket: {}", BUCKET_NAME);
		logger.info("===========================================\n");

		Optional<AWSCredentials> credentials = new AWSHelper().getCredentials();
		credentials.ifPresent(c -> simpleTest(() -> uploadOneFile(

				S3ClientBuilder.build(c, s -> s.setRegion(getRegion(US_EAST_1)))), "S3_test_upload_"));

	}

	public void runDownload() {

		logger.info("===========================================");
		logger.info("Starting Amazon S3 Download test");
		logger.info("Download Files from Bucket: {}", BUCKET_NAME);
		logger.info("===========================================\n");

		Optional<AWSCredentials> credentials = new AWSHelper().getCredentials();

		credentials
				.ifPresent(
						c -> simpleTestList(() -> files.stream()
								.map(f -> downloadOneFile(
										S3ClientBuilder.build(c, s -> s.setRegion(getRegion(US_EAST_1))), f.getName()))
								.collect(toList()), "S3_test_download_"));
	}

	private LatencyTestResponse uploadOneFile(AmazonS3Client amazonS3Client) {
		File randomFile = FileHelper.generateRandomFile();
		Instant start = Instant.now();
		PutObjectResult putResult = amazonS3Client.putObject(BUCKET_NAME, randomFile.getName(), randomFile);
		Instant end = Instant.now();
		files.add(randomFile);
		return new LatencyTestResponse(putResult.getETag(), Duration.between(start, end));
	}

	private LatencyTestResponse downloadOneFile(AmazonS3Client amazonS3Client, String pathName) {
		Instant start = Instant.now();
		S3Object s3Object = amazonS3Client.getObject(BUCKET_NAME, pathName);
		Instant end = Instant.now();
		return new LatencyTestResponse(s3Object.getKey(), Duration.between(start, end));
	}
}
