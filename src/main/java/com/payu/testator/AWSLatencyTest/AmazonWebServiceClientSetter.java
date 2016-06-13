package com.payu.testator.AWSLatencyTest;

import java.util.function.Consumer;

import com.amazonaws.AmazonWebServiceClient;

@FunctionalInterface
public interface AmazonWebServiceClientSetter extends Consumer<AmazonWebServiceClient> {

}