package com.payu.testator.AWSLatencyTest;

import com.payu.testator.AWSLatencyTest.S3.S3Tester;

public class Application {

	public static void main(String[] args) {
		//new SQSTester().runWriteTest();
		//new SQSTester().runReceiveTest();

		S3Tester s3Tester = new S3Tester();
		s3Tester.runUpload();
		s3Tester.runDownload();
		
	}
}

