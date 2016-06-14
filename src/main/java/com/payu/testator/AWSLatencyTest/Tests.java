package com.payu.testator.AWSLatencyTest;

import java.util.Optional;
import java.util.stream.Stream;

import com.payu.testator.AWSLatencyTest.Dynamo.DynamoTester;
import com.payu.testator.AWSLatencyTest.S3.S3Tester;
import com.payu.testator.AWSLatencyTest.SQS.SQSTester;

public enum Tests {
	SQSOPTION("SQS") {
		@Override
		public void callTest() {
			SQSTester sqsTester = new SQSTester();
			sqsTester.runWriteTest();
			sqsTester.runReceiveTest();
		}

		@Override
		public void callTest(Long amount) {
			SQSTester sqsTester = new SQSTester(amount);
			sqsTester.runWriteTest();
			sqsTester.runReceiveTest();

		}
	},
	S3OPTION("S3") {
		@Override
		public void callTest() {
			S3Tester s3Tester = new S3Tester();
			s3Tester.runUpload();
			s3Tester.runDownload();

		}

		@Override
		public void callTest(Long amount) {
			S3Tester s3Tester = new S3Tester(amount);
			s3Tester.runUpload();
			s3Tester.runDownload();

		}
	},
	DYNAMODBOPTION("D") {
		@Override
		public void callTest() {
			DynamoTester dynamoTester = new DynamoTester();
			dynamoTester.runPutTest();
			dynamoTester.runGetTest();

		}

		@Override
		public void callTest(Long amount) {
			DynamoTester dynamoTester = new DynamoTester(amount);
			dynamoTester.runPutTest();
			dynamoTester.runGetTest();

		}
	};

	private String key;

	Tests(String key) {
		this.key = key;
	}

	public String getKey() {
		return key;
	}

	public abstract void callTest();

	public abstract void callTest(Long amount);

	public static Optional<Tests> from(String selectedTest) {

		return Stream.of(values()).filter(t -> t.getKey().equalsIgnoreCase(selectedTest)).findFirst();
	}
}
