package com.example.framework.utils;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;

/**
 * Performance tracking utility for measuring response times
 * Tracks page load times, element interaction times, and API response times
 */
public class PerformanceUtils {
    private static final Logger logger = LogManager.getLogger(PerformanceUtils.class);
    private static ThreadLocal<Long> operationStartTime = new ThreadLocal<>();
    private static Properties performanceConfig;

    static {
        loadPerformanceConfig();
    }

    /**
     * Load performance configuration
     */
    private static void loadPerformanceConfig() {
        performanceConfig = new Properties();
        try {
            performanceConfig.load(new FileInputStream("src/test/resources/performance.properties"));
            logger.info("Performance configuration loaded");
        } catch (IOException e) {
            logger.warn("Performance configuration file not found, using defaults");
            // Set default thresholds
            performanceConfig.setProperty("page.load.timeout.ms", "5000");
            performanceConfig.setProperty("element.interaction.timeout.ms", "2000");
            performanceConfig.setProperty("api.response.timeout.ms", "3000");
        }
    }

    /**
     * Start performance measurement
     */
    public static void startTimer() {
        operationStartTime.set(System.currentTimeMillis());
    }

    /**
     * Stop performance measurement and log duration
     */
    public static long stopTimer(String operationName) {
        Long startTime = operationStartTime.get();
        if (startTime == null) {
            logger.warn("Timer was not started for operation: {}", operationName);
            return 0;
        }

        long endTime = System.currentTimeMillis();
        long duration = endTime - startTime;
        logger.info("Operation '{}' completed in {}ms", operationName, duration);
        operationStartTime.remove();
        return duration;
    }

    /**
     * Assert response time is within threshold
     */
    public static void assertResponseTime(String operationName, long actualTime, long thresholdMs) {
        if (actualTime > thresholdMs) {
            logger.warn("PERFORMANCE WARNING: Operation '{}' exceeded threshold. " +
                    "Actual: {}ms, Threshold: {}ms", operationName, actualTime, thresholdMs);
        } else {
            logger.info("PERFORMANCE OK: Operation '{}' completed within threshold. " +
                    "Actual: {}ms, Threshold: {}ms", operationName, actualTime, thresholdMs);
        }
    }

    /**
     * Measure and assert page load time
     */
    public static void assertPageLoadTime(WebDriver driver, long thresholdMs) {
        try {
            JavascriptExecutor js = (JavascriptExecutor) driver;
            long navigationStart = ((Number) js.executeScript(
                "return window.performance.timing.navigationStart")).longValue();
            long loadEnd = ((Number) js.executeScript(
                "return window.performance.timing.loadEventEnd")).longValue();
            long pageLoadTime = loadEnd - navigationStart;

            assertResponseTime("Page Load", pageLoadTime, thresholdMs);
        } catch (Exception e) {
            logger.error("Error measuring page load time", e);
        }
    }

    /**
     * Get default page load timeout from config
     */
    public static long getPageLoadThreshold() {
        return Long.parseLong(
            performanceConfig.getProperty("page.load.timeout.ms", "5000"));
    }

    /**
     * Get default element interaction timeout from config
     */
    public static long getElementInteractionThreshold() {
        return Long.parseLong(
            performanceConfig.getProperty("element.interaction.timeout.ms", "2000"));
    }

    /**
     * Get default API response timeout from config
     */
    public static long getApiResponseThreshold() {
        return Long.parseLong(
            performanceConfig.getProperty("api.response.timeout.ms", "3000"));
    }

    /**
     * Compare actual time against baseline and detect regression
     */
    public static void compareAgainstBaseline(String operationName, long actualTime, long baselineMs) {
        double threshold = 1.2; // 20% threshold
        long allowedTime = (long) (baselineMs * threshold);

        if (actualTime > allowedTime) {
            logger.error("PERFORMANCE REGRESSION: Operation '{}' exceeded baseline. " +
                    "Baseline: {}ms, Actual: {}ms, Allowed: {}ms ({}% over)",
                    operationName, baselineMs, actualTime, allowedTime,
                    ((actualTime - baselineMs) * 100 / baselineMs));
        } else {
            logger.info("PERFORMANCE OK: Operation '{}' within baseline. " +
                    "Baseline: {}ms, Actual: {}ms",
                    operationName, baselineMs, actualTime);
        }
    }

    /**
     * Clear thread-local storage
     */
    public static void cleanup() {
        operationStartTime.remove();
    }
}
