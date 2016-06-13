package com.payu.testator.AWSLatencyTest;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.payu.testator.AWSLatencyTest.SQS.SQSTester;

public class FileHelper {

	private static final Logger logger = LoggerFactory.getLogger(SQSTester.class);

	public static void write(String fileName, String content) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d-MMM-uuuu HH:mm:ss");
		String file = fileName + formatter.format(LocalDateTime.now()) + ".txt";
		logger.info("Writing result file {} ", file);
		try (BufferedWriter writer = Files.newBufferedWriter(Paths.get(file))) {
			writer.write(content);
		} catch (IOException e) {
			logger.error("Error on write response test file. ", e);
		}
		logger.info("success to write file.");
	}

	public static File generateRandomFile() {
		File tempFile = null;
		try {
			tempFile = File.createTempFile("Test_file_", null);
		} catch (IOException e) {
			logger.error("Error on write random test file. ", e);
		}
		logger.info("success to write random test file: {}" + tempFile.getName());
		return tempFile;
	}
}
