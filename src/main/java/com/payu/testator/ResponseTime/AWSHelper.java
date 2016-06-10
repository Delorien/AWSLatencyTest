package com.payu.testator.ResponseTime;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;

public class AWSHelper {

	private final Logger logger = LoggerFactory.getLogger(AWSHelper.class);

	public Optional<AWSCredentials> getCredentials() {
		AWSCredentials credentials = null;
		try {
			credentials = new ProfileCredentialsProvider().getCredentials();
		} catch (Exception e) {

			String message = "Cannot load the credentials from the credential profiles file. "
					+ "Please make sure that your credentials file is at the correct "
					+ "location (~/.aws/credentials), and is in valid format.";

			logger.error(message, e);
		}

		return Optional.ofNullable(credentials);
	}

}