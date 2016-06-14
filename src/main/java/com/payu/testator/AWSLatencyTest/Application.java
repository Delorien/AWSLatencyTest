package com.payu.testator.AWSLatencyTest;

import static com.payu.testator.AWSLatencyTest.PropertieKeys.AMOUNT_TEST;
import static com.payu.testator.AWSLatencyTest.Tests.DYNAMODBOPTION;
import static com.payu.testator.AWSLatencyTest.Tests.S3OPTION;
import static com.payu.testator.AWSLatencyTest.Tests.SQSOPTION;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.NoSuchElementException;

public class Application {

	private final BufferedReader reader;

	{
		reader = new BufferedReader(new InputStreamReader(System.in));
	}

	public static void main(String[] args) {

		Application application = new Application();
		application.initialDialog();

	}

	public void initialDialog() {
		System.out.println("Please enter the desired test: ");
		System.out.println("Options: (" + SQSOPTION.getKey() + ")SQS, (" + S3OPTION.getKey() + ")S3, ("
				+ DYNAMODBOPTION.getKey() + ")DynamoBD");
		try {
			String option = reader.readLine();
			Tests test = Tests.from(option).orElseThrow(() -> new NoSuchElementException());
			Long amount = askAmount();

			if (amount > 0L) {
				test.callTest(amount);
			} else {
				test.callTest();
			}

			finishDialog();

		} catch (Exception e) {
			System.out.println("Invalid option!! Please try again.");
			initialDialog();
		}
	}

	private void finishDialog() throws IOException {
		System.out.println("Do you want to perform new tests?: ");
		System.out.println("Press (y) for yes or (q) to quit:");
		String option = reader.readLine();

		if (option.equalsIgnoreCase("y")) {
			initialDialog();
		} else if (!option.equalsIgnoreCase("q")) {
			System.out.println("Invalid Option!");
			finishDialog();
		} else {
			System.out.println("Thanks pal!");
		}
	}

	private Long askAmount() throws IOException {
		System.out.println(
				"The default amount of tests to be performed is: " + PropertiesLoader.get(AMOUNT_TEST.getKey()));
		System.out.println("Specify a new quantity or press enter to proceed: ");
		String amount = reader.readLine();
		return !amount.isEmpty() ? Long.valueOf(amount) : 0L;
	}

}
