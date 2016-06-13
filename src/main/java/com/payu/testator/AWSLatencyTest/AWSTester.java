package com.payu.testator.AWSLatencyTest;

import static com.payu.testator.AWSLatencyTest.PropertieKeys.AMOUNT_TEST;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.amazonaws.auth.AWSCredentials;

public abstract class AWSTester {

	protected static final String DEFAULT_AMOUNT = "5";

	protected static final Long AMOUNT;

	protected final Logger logger = LoggerFactory.getLogger(this.getClass());

	protected Optional<AWSCredentials> credentials;

	static {
		AMOUNT = Long.valueOf(Optional.ofNullable(PropertiesLoader.get(AMOUNT_TEST.getKey()))
				.filter(s -> !s.isEmpty()).orElse(DEFAULT_AMOUNT));
	}

	public AWSTester() {
		credentials = new AWSHelper().getCredentials();
	}

}
