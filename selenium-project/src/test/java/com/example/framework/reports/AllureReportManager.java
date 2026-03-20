package com.example.framework.reports;

import io.qameta.allure.Allure;
import io.qameta.allure.AllureLifecycle;
import io.qameta.allure.Step;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

/**
 * Allure Report Manager
 * Handles Allure report integration and step logging
 */
public class AllureReportManager {
    private static final Logger logger = LogManager.getLogger(AllureReportManager.class);

    /**
     * Log a step in Allure report
     */
    @Step("{stepName}")
    public static void logStep(String stepName) {
        logger.info("Step: {}", stepName);
        Allure.step(stepName);
    }

    /**
     * Log test action as step
     */
    public static void step(String action, String description) {
        Allure.step(action, () -> {
            logger.info("{} - {}", action, description);
        });
    }

    /**
     * Add attachment to Allure report
     */
    public static void attachFile(String name, String filePath, String mimeType) {
        try {
            File file = new File(filePath);
            if (file.exists()) {
                Allure.addAttachment(name, mimeType, new FileInputStream(file), 
                    filePath.substring(filePath.lastIndexOf(".") + 1));
                logger.info("File attached to Allure report: {}", name);
            } else {
                logger.warn("File not found for attachment: {}", filePath);
            }
        } catch (IOException e) {
            logger.error("Error attaching file to Allure report", e);
        }
    }

    /**
     * Attach screenshot to Allure report
     */
    public static void attachScreenshot(String screenshotPath) {
        try {
            attachFile("Screenshot", screenshotPath, "image/png");
        } catch (Exception e) {
            logger.error("Error attaching screenshot to Allure", e);
        }
    }

    /**
     * Attach logs to Allure report
     */
    public static void attachLogs(String logContent) {
        try {
            Allure.addAttachment("Logs", "text/plain", logContent);
            logger.info("Logs attached to Allure report");
        } catch (Exception e) {
            logger.error("Error attaching logs to Allure", e);
        }
    }

    /**
     * Set test parameter
     */
    public static void setParameter(String name, String value) {
        Allure.parameter(name, value);
        logger.info("Parameter set: {} = {}", name, value);
    }

    /**
     * Add link to TMS (Test Management System)
     */
    public static void addTestAsLink(String testId) {
        Allure.link("Test ID: " + testId, testId);
    }

    /**
     * Add issue link
     */
    public static void addIssueLink(String issueId) {
        Allure.issue("Issue ID: " + issueId, issueId);
    }

    /**
     * Log test result details
     */
    public static void logTestDetails(String testName, String description, String severity) {
        logger.info("Test: {} | Description: {} | Severity: {}", testName, description, severity);
    }

    /**
     * Get Allure lifecycle for advanced operations
     */
    public static AllureLifecycle getAllureLifecycle() {
        return Allure.getLifecycle();
    }
}
