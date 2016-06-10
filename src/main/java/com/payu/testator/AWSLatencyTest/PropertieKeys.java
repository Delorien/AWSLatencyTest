package com.payu.testator.AWSLatencyTest;

public enum PropertieKeys {

	QUEUEURL("queue.url"), QUEUE_AMOUNT_TEST("queue.amountTest");

	private String key;

	PropertieKeys(String key) {
		this.key = key;
	}

	public String getKey() {
		return key;
	}
}
