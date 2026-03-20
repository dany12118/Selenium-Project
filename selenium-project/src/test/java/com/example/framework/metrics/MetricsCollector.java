package com.example.framework.metrics;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Metrics collection for test execution
 * Tracks execution time, pass/fail rates, flakiness, and other metrics
 */
public class MetricsCollector {
    private static final Logger logger = LogManager.getLogger(MetricsCollector.class);
    private static ThreadLocal<MetricsHolder> metricsHolder = new ThreadLocal<>();

    public static class MetricsHolder {
        public String testName;
        public long startTime;
        public long endTime;
        public String status; // PASS, FAIL, SKIP
        public int retryCount = 0;
        public long executionTimeMs;
        public String errorMessage;
        public String errorStackTrace;
        public LocalDateTime timestamp;

        public MetricsHolder(String testName) {
            this.testName = testName;
            this.timestamp = LocalDateTime.now();
            this.startTime = System.currentTimeMillis();
        }

        public void complete(String status) {
            this.endTime = System.currentTimeMillis();
            this.executionTimeMs = this.endTime - this.startTime;
            this.status = status;
        }
    }

    /**
     * Start test metrics collection
     */
    public static void startMetrics(String testName) {
        MetricsHolder holder = new MetricsHolder(testName);
        metricsHolder.set(holder);
        logger.info("Started metrics collection for test: {}", testName);
    }

    /**
     * Record test completion
     */
    public static void recordTestCompletion(String status) {
        MetricsHolder holder = metricsHolder.get();
        if (holder != null) {
            holder.complete(status);
            logger.info("Test {} completed with status: {} ({}ms)", 
                holder.testName, status, holder.executionTimeMs);
        }
    }

    /**
     * Record retry attempt
     */
    public static void recordRetry() {
        MetricsHolder holder = metricsHolder.get();
        if (holder != null) {
            holder.retryCount++;
            logger.info("Test {} retry #{}", holder.testName, holder.retryCount);
        }
    }

    /**
     * Record error
     */
    public static void recordError(String errorMessage, String stackTrace) {
        MetricsHolder holder = metricsHolder.get();
        if (holder != null) {
            holder.errorMessage = errorMessage;
            holder.errorStackTrace = stackTrace;
            logger.error("Recorded error for test {}: {}", holder.testName, errorMessage);
        }
    }

    /**
     * Get current test metrics
     */
    public static MetricsHolder getCurrentMetrics() {
        return metricsHolder.get();
    }

    /**
     * Write metrics to JSON file
     */
    public static void exportMetricsToJson(String filePath) {
        MetricsHolder holder = metricsHolder.get();
        if (holder != null) {
            try {
                ObjectMapper mapper = new ObjectMapper();
                Map<String, Object> metricsMap = new HashMap<>();
                metricsMap.put("testName", holder.testName);
                metricsMap.put("status", holder.status);
                metricsMap.put("executionTimeMs", holder.executionTimeMs);
                metricsMap.put("retryCount", holder.retryCount);
                metricsMap.put("timestamp", holder.timestamp.toString());
                metricsMap.put("errorMessage", holder.errorMessage);

                new File(filePath).getParentFile().mkdirs();
                try (FileWriter writer = new FileWriter(filePath, true)) {
                    writer.write(mapper.writeValueAsString(metricsMap));
                    writer.write(System.lineSeparator());
                }
                logger.info("Metrics exported to: {}", filePath);
            } catch (IOException e) {
                logger.error("Failed to export metrics", e);
            }
        }
    }

    /**
     * Check if test is slow (exceeds threshold)
     */
    public static boolean isSlowTest(long thresholdMs) {
        MetricsHolder holder = metricsHolder.get();
        if (holder != null) {
            return holder.executionTimeMs > thresholdMs;
        }
        return false;
    }

    /**
     * Get execution time
     */
    public static long getExecutionTime() {
        MetricsHolder holder = metricsHolder.get();
        return holder != null ? holder.executionTimeMs : 0;
    }

    /**
     * Clear metrics
     */
    public static void clearMetrics() {
        metricsHolder.remove();
    }
}
