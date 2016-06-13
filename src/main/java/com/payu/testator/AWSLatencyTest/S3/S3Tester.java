package com.payu.testator.AWSLatencyTest.S3;

import static com.amazonaws.regions.Region.getRegion;
import static com.amazonaws.regions.Regions.US_EAST_1;
import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toList;

import java.io.File;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;
import java.util.stream.Stream;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
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
		credentials.ifPresent(c -> executeTest(
				() -> uploadOneFile(S3ClientBuilder.build(c, s -> s.setRegion(getRegion(US_EAST_1)))), "upload"));

	}

	public void runDownload() {

		logger.info("===========================================");
		logger.info("Starting Amazon S3 Upload test");
		logger.info("Download Files of Bucket: {}", BUCKET_NAME);
		logger.info("===========================================\n");

		Optional<AWSCredentials> credentials = new AWSHelper().getCredentials();
		credentials.ifPresent(
				c -> executeDownloadTest(S3ClientBuilder.build(c, s -> s.setRegion(getRegion(US_EAST_1)))));
	}

	private void executeTest(Supplier<LatencyTestResponse> supplier, String name) {
		try {
			List<LatencyTestResponse> results = Stream.generate(supplier).limit(AMOUNT).collect(toList());

			String testResponses = results.stream().map(Object::toString).collect(joining("\n"));
			testResponses += "\nAvarage = "
					+ results.stream().mapToDouble(r -> r.getDuration().toMillis()).average().orElse(0.0);

			logger.info(testResponses);
			FileHelper.write("S3_test_" + name + "_", testResponses);
			logger.info("Upload tests successfully completed.");
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

	private void executeDownloadTest(AmazonS3Client amazonS3Client) {
		try {

			List<S3ResponseDownload> results = files.stream().map(f -> downloadOneFile(amazonS3Client, f.getName()))
					.collect(toList());

			String testResponses = results.stream().map(Object::toString).collect(joining("\n"));
			testResponses += "\nAvarage = "
					+ results.stream().mapToDouble(r -> r.getDuration().toMillis()).average().orElse(0.0);

			logger.info(testResponses);
			FileHelper.write("S3_test_download_", testResponses);
			logger.info("Download tests successfully completed.");
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

	private S3ResponseDownload downloadOneFile(AmazonS3Client amazonS3Client, String pathName) {
		Instant start = Instant.now();
		S3Object s3Object = amazonS3Client.getObject(BUCKET_NAME, pathName);
		Instant end = Instant.now();
		return new S3ResponseDownload(s3Object, Duration.between(start, end));
	}

	private S3ResponseUpload uploadOneFile(AmazonS3Client amazonS3Client) {
		File randomFile = FileHelper.generateRandomFile();
		Instant start = Instant.now();
		PutObjectResult putResult = amazonS3Client.putObject(BUCKET_NAME, randomFile.getName(), randomFile);
		Instant end = Instant.now();
		files.add(randomFile);
		return new S3ResponseUpload(putResult, Duration.between(start, end));
	}

}
