package com.example.framework.utils;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

/**
 * Screenshot utility for capturing browser screenshots
 * Used for test failure documentation and reporting
 */
public class ScreenshotUtils {
    private static final Logger logger = LogManager.getLogger(ScreenshotUtils.class);
    private static final String SCREENSHOT_DIR = "target/screenshots/";
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss_SSS");

    static {
        // Create screenshots directory if it doesn't exist
        File screenshotDir = new File(SCREENSHOT_DIR);
        if (!screenshotDir.exists()) {
            screenshotDir.mkdirs();
            logger.info("Created screenshot directory: {}", SCREENSHOT_DIR);
        }
    }

    /**
     * Capture screenshot and save to file
     */
    public static String captureScreenshot(WebDriver driver, String testName) {
        try {
            // Create screenshot directory if needed
            File screenshotDir = new File(SCREENSHOT_DIR);
            if (!screenshotDir.exists()) {
                screenshotDir.mkdirs();
            }

            // Generate screenshot filename with timestamp
            String timestamp = LocalDateTime.now().format(formatter);
            String screenshotName = testName + "_" + timestamp + ".png";
            String screenshotPath = SCREENSHOT_DIR + screenshotName;

            // Take screenshot
            TakesScreenshot ts = (TakesScreenshot) driver;
            File sourceFile = ts.getScreenshotAs(OutputType.FILE);
            File destinationFile = new File(screenshotPath);

            // Copy to destination
            FileUtils.copyFile(sourceFile, destinationFile);
            logger.info("Screenshot captured: {}", screenshotPath);

            return screenshotPath;
        } catch (IOException e) {
            logger.error("Error capturing screenshot", e);
            return null;
        } catch (Exception e) {
            logger.error("Error taking screenshot", e);
            return null;
        }
    }

    /**
     * Capture screenshot on failure
     */
    public static String captureScreenshotOnFailure(WebDriver driver, String testName) {
        logger.warn("Capturing screenshot for failed test: {}", testName);
        return captureScreenshot(driver, testName + "_FAILED");
    }

    /**
     * Capture screenshot with custom name
     */
    public static String captureScreenshot(WebDriver driver, String testName, String step) {
        try {
            String timestamp = LocalDateTime.now().format(formatter);
            String screenshotName = testName + "_" + step + "_" + timestamp + ".png";
            String screenshotPath = SCREENSHOT_DIR + screenshotName;

            File screenshotDir = new File(SCREENSHOT_DIR);
            if (!screenshotDir.exists()) {
                screenshotDir.mkdirs();
            }

            TakesScreenshot ts = (TakesScreenshot) driver;
            File sourceFile = ts.getScreenshotAs(OutputType.FILE);
            File destinationFile = new File(screenshotPath);

            FileUtils.copyFile(sourceFile, destinationFile);
            logger.info("Screenshot captured for step '{}': {}", step, screenshotPath);

            return screenshotPath;
        } catch (IOException e) {
            logger.error("Error capturing screenshot for step: {}", step, e);
            return null;
        }
    }

    /**
     * Get screenshot directory path
     */
    public static String getScreenshotDirectory() {
        return SCREENSHOT_DIR;
    }

    /**
     * Delete old screenshots (cleanup)
     */
    public static void deleteOldScreenshots(int daysOld) {
        try {
            File dir = new File(SCREENSHOT_DIR);
            if (dir.exists()) {
                long cutOffTime = System.currentTimeMillis() - (daysOld * 24 * 60 * 60 * 1000L);
                File[] files = dir.listFiles();
                
                if (files != null) {
                    for (File file : files) {
                        if (file.lastModified() < cutOffTime) {
                            if (file.delete()) {
                                logger.info("Deleted old screenshot: {}", file.getName());
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            logger.error("Error deleting old screenshots", e);
        }
    }

    /**
     * Verify screenshot file exists
     */
    public static boolean screenshotExists(String screenshotPath) {
        try {
            File file = new File(screenshotPath);
            return file.exists() && file.length() > 0;
        } catch (Exception e) {
            logger.error("Error verifying screenshot: {}", screenshotPath, e);
            return false;
        }
    }
}
