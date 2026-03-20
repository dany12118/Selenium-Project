package com.example.framework.listeners;

import org.testng.IRetryAnalyzer;
import org.testng.ITestResult;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Retry analyzer for flaky tests
 * Automatically retries failed tests up to MAX_RETRY_COUNT times
 */
public class TestRetryAnalyzer implements IRetryAnalyzer {
    private static final Logger logger = LogManager.getLogger(TestRetryAnalyzer.class);
    private static final int MAX_RETRY_COUNT = 2;
    private int retryCount = 0;

    /**
     * Determine if test should be retried
     */
    @Override
    public boolean retry(ITestResult result) {
        if (!result.isSuccess()) {
            if (retryCount < MAX_RETRY_COUNT) {
                retryCount++;
                logger.warn("Test {} failed. Retrying... Attempt {}/{}",
                    result.getMethod().getMethodName(),
                    retryCount,
                    MAX_RETRY_COUNT);
                result.setStatus(ITestResult.SKIP);
                return true;
            }
        }
        return false;
    }

    /**
     * Get retry count
     */
    public int getRetryCount() {
        return retryCount;
    }
}
