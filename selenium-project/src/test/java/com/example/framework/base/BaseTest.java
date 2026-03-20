package com.example.framework.base;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import com.example.framework.driver.DriverManager;
import com.example.framework.metrics.MetricsCollector;
import com.example.framework.reports.ExtentReportManager;
import com.example.framework.utils.ScreenshotUtils;
import java.lang.reflect.Method;

/**
 * Base test class for all tests
 * Handles setup/teardown and provides common utilities
 */
public class BaseTest {
    protected WebDriver driver;
    protected static Logger logger = LogManager.getLogger(BaseTest.class);

    @BeforeMethod
    public void setUp(Method method) {
        // Get test method name from TestNG's Method object
        String testMethodName = method.getName();
        logger.info("=== Setting up test: {} ===", testMethodName);
        driver = DriverManager.getDriver();
        DriverManager.waitForElementPresent(10);
        MetricsCollector.startMetrics(testMethodName);
        ExtentReportManager.createTest(testMethodName);
        logger.info("=== Test setup completed ===");
    }

    @AfterMethod
    public void tearDown(ITestResult result) {
        logger.info("=== Tearing down test ===");
        try {
            String testStatus = result.isSuccess() ? "PASS" : "FAIL";
            String testName = result.getMethod().getMethodName();
            
            // Log failure details if test failed
            if (!result.isSuccess()) {
                logger.error("Test failed: {}", result.getThrowable());
                ExtentReportManager.logFail("Test failed: " + result.getThrowable().getMessage());
                
                // Capture screenshot on failure
                try {
                    String screenshotPath = ScreenshotUtils.captureScreenshotOnFailure(driver, testName);
                    if (screenshotPath != null) {
                        logger.info("Screenshot captured at: {}", screenshotPath);
                        ExtentReportManager.logFail("Screenshot saved: " + screenshotPath);
                        // Attach screenshot to report
                        ExtentReportManager.attachScreenshot(screenshotPath);
                    }
                } catch (Exception e) {
                    logger.error("Error capturing screenshot for failed test", e);
                }
            } else {
                ExtentReportManager.logPass("Test passed successfully");
            }
            
            MetricsCollector.recordTestCompletion(testStatus);
            ExtentReportManager.flushReports();
        } catch (Exception e) {
            logger.error("Error during teardown", e);
        } finally {
            DriverManager.quitDriver();
        }
        logger.info("=== Test teardown completed ===");
    }
}
