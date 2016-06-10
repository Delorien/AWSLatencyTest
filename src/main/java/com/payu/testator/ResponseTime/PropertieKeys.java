package com.payu.testator.ResponseTime;

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
