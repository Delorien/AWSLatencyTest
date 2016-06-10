package com.payu.testator.AWSLatencyTest;

public class Application {

	public static void main(String[] args) {
		new SQSTester().runWriteTest();
		new SQSTester().runReceiveTest();

	}
}
