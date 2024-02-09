package com.graphaware.strategies;

import org.testcontainers.containers.wait.strategy.HttpWaitStrategy;
import org.testcontainers.containers.wait.strategy.LogMessageWaitStrategy;
import org.testcontainers.containers.wait.strategy.WaitStrategy;

import java.net.HttpURLConnection;

public class WaitingStrategies {

    public static final WaitStrategy WAIT_FOR_BOLT = new LogMessageWaitStrategy()
        .withRegEx(String.format(".*Bolt enabled on .*:%d\\.\n", 7687));

    public static final WaitStrategy WAIT_FOR_HTTP = new HttpWaitStrategy()
        .forPort(7474)
        .forStatusCodeMatching(response -> response == HttpURLConnection.HTTP_OK);
}
