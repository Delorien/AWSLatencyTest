package com.payu.testator.AWSLatencyTest;

import com.payu.testator.AWSLatencyTest.Dynamo.DynamoTester;

public class Application {

	public static void main(String[] args) {
		
//		new SQSTester().runWriteTest();
//		new SQSTester().runReceiveTest();

//		S3Tester s3Tester = new S3Tester();
//		s3Tester.runUpload();
//		s3Tester.runDownload();
		
		DynamoTester dynamoTester =  new DynamoTester();
		dynamoTester.runPutTest();
		dynamoTester.runGetTest();
		
	}
}

