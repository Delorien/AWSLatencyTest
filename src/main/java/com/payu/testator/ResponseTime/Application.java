package com.payu.testator.ResponseTime;

public class Application {

	public static void main(String[] args) {
		new SQSTester().runWriteTest();
		new SQSTester().runReceiveTest();

	}
}
