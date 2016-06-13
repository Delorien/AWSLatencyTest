package com.payu.testator.AWSLatencyTest;

public enum PropertieKeys {

	QUEUEURL("queue.url"), AMOUNT_TEST("queue.amountTest"), BUCKET("bucket.name"), TABLE("dynamo.table");

	private String key;

	PropertieKeys(String key) {
		this.key = key;
	}

	public String getKey() {
		return key;
	}
}
