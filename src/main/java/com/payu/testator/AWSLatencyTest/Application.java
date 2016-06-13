package com.payu.testator.AWSLatencyTest;

import com.payu.testator.AWSLatencyTest.SQS.SQSTester;

public class Application {

	public static void main(String[] args) {
		new SQSTester().runWriteTest();
		new SQSTester().runReceiveTest();

	}
}
