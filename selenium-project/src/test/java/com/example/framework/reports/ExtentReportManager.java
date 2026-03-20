package com.example.framework.reports;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Extent Report Manager
 * Handles test report generation and management
 */
public class ExtentReportManager {
    private static final Logger logger = LogManager.getLogger(ExtentReportManager.class);
    private static ExtentReports extent;
    private static final ThreadLocal<ExtentTest> testThreadLocal = new ThreadLocal<>();
    private static final String REPORT_PATH = "target/reports/";
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss");

    static {
        initializeReport();
    }

    /**
     * Initialize Extent Report
     */
    private static void initializeReport() {
        try {
            String timestamp = LocalDateTime.now().format(formatter);
            String reportFile = REPORT_PATH + "TestReport_" + timestamp + ".html";
            
            // Create report directory if it doesn't exist
            File reportDir = new File(REPORT_PATH);
            if (!reportDir.exists()) {
                reportDir.mkdirs();
            }

            ExtentSparkReporter sparkReporter = new ExtentSparkReporter(reportFile);
            extent = new ExtentReports();
            extent.attachReporter(sparkReporter);
            
            // Set metadata
            extent.setSystemInfo("Application", "Selenium Test Framework");
            extent.setSystemInfo("Environment", "Test");
            extent.setSystemInfo("User", System.getProperty("user.name"));
            extent.setSystemInfo("OS", System.getProperty("os.name"));
            
            logger.info("Extent Report initialized at: {}", reportFile);
        } catch (Exception e) {
            logger.error("Error initializing Extent Report", e);
        }
    }

    /**
     * Create a new test
     */
    public static void createTest(String testName) {
        try {
            ExtentTest test = extent.createTest(testName);
            testThreadLocal.set(test);
            logger.info("Test created: {}", testName);
        } catch (Exception e) {
            logger.error("Error creating test: {}", testName, e);
        }
    }

    /**
     * Get current test
     */
    public static ExtentTest getTest() {
        return testThreadLocal.get();
    }

    /**
     * Log test info
     */
    public static void logInfo(String message) {
        ExtentTest test = testThreadLocal.get();
        if (test != null) {
            test.info(message);
        }
    }

    /**
     * Log test pass
     */
    public static void logPass(String message) {
        ExtentTest test = testThreadLocal.get();
        if (test != null) {
            test.pass(message);
        }
    }

    /**
     * Log test fail
     */
    public static void logFail(String message) {
        ExtentTest test = testThreadLocal.get();
        if (test != null) {
            test.fail(message);
        }
    }

    /**
     * Log test warning
     */
    public static void logWarning(String message) {
        ExtentTest test = testThreadLocal.get();
        if (test != null) {
            test.warning(message);
        }
    }

    /**
     * Attach screenshot to test report
     */
    public static void attachScreenshot(String screenshotPath) {
        try {
            ExtentTest test = testThreadLocal.get();
            if (test != null && screenshotPath != null) {
                File screenshotFile = new File(screenshotPath);
                if (screenshotFile.exists()) {
                    test.addScreenCaptureFromPath(screenshotPath);
                    logger.info("Screenshot attached to report: {}", screenshotPath);
                } else {
                    logger.warn("Screenshot file not found: {}", screenshotPath);
                }
            }
        } catch (Exception e) {
            logger.error("Error attaching screenshot", e);
        }
    }

    /**
     * Flush reports to file
     */
    public static void flushReports() {
        try {
            if (extent != null) {
                extent.flush();
            }
            testThreadLocal.remove();
            logger.info("Report flushed successfully");
        } catch (Exception e) {
            logger.error("Error flushing report", e);
        }
    }

    /**
     * Reset Extent Report
     */
    public static void reset() {
        try {
            testThreadLocal.remove();
            if (extent != null) {
                extent = null;
            }
            initializeReport();
        } catch (Exception e) {
            logger.error("Error resetting report", e);
        }
    }
}
