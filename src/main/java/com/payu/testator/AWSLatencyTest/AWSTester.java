package com.payu.testator.AWSLatencyTest;

import static com.payu.testator.AWSLatencyTest.PropertieKeys.AMOUNT_TEST;
import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toList;

import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;
import java.util.stream.Stream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.auth.AWSCredentials;

public abstract class AWSTester {

	protected static final String DEFAULT_AMOUNT = "0";

	protected Long amount;

	protected final Logger logger = LoggerFactory.getLogger(this.getClass());

	protected Optional<AWSCredentials> credentials;

	{
		amount = Long.valueOf(Optional.ofNullable(PropertiesLoader.get(AMOUNT_TEST.getKey())).filter(s -> !s.isEmpty())
				.orElse(DEFAULT_AMOUNT));
	}

	public AWSTester() {
		credentials = new AWSHelper().getCredentials();
	}

	public AWSTester(Long amount) {
		this();
		this.amount = amount;
	}

	protected void simpleTest(Supplier<LatencyTestResponse> supplier, String name) {
		try {
			List<LatencyTestResponse> results = Stream.generate(supplier).limit(amount).collect(toList());
			this.print(results, name);
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

	protected void simpleTestList(Supplier<List<LatencyTestResponse>> supplier, String name) {

		try {
			List<LatencyTestResponse> results = supplier.get();
			this.print(results, name);
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

	private void print(List<LatencyTestResponse> results, String name) {

		String testResponses = results.stream().map(Object::toString).collect(joining("\n"));
		testResponses += "\nAvarage = "
				+ results.stream().mapToDouble(r -> r.getDuration().toMillis()).average().orElse(0.0);

		logger.info(testResponses);
		FileHelper.write(name, testResponses);
		logger.info("Tests successfully completed.");

	}

}
