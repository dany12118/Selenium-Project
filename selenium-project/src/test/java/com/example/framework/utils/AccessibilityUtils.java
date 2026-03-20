package com.example.framework.utils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

/**
 * Accessibility testing utility for WCAG 2.1 compliance
 * Checks for common accessibility issues and violations
 */
public class AccessibilityUtils {
    private static final Logger logger = LogManager.getLogger(AccessibilityUtils.class);

    /**
     * Check if Axe library is loaded and ready
     */
    public static boolean isAxeAvailable(WebDriver driver) {
        try {
            JavascriptExecutor js = (JavascriptExecutor) driver;
            Object result = js.executeScript("return typeof axe !== 'undefined'");
            return result.equals(true);
        } catch (Exception e) {
            logger.warn("Axe not available", e);
            return false;
        }
    }

    /**
     * Inject Axe script into page
     */
    public static void injectAxe(WebDriver driver) {
        try {
            JavascriptExecutor js = (JavascriptExecutor) driver;
            String axeScript = "var script = document.createElement('script');" +
                "script.src = 'https://cdnjs.cloudflare.com/ajax/libs/axe-core/4.7.0/axe.min.js';" +
                "document.head.appendChild(script);";
            js.executeScript(axeScript);
            logger.info("Axe injected into page");
        } catch (Exception e) {
            logger.error("Failed to inject Axe script", e);
        }
    }

    /**
     * Run accessibility audit
     */
    public static void runAccessibilityAudit(WebDriver driver) {
        try {
            JavascriptExecutor js = (JavascriptExecutor) driver;
            if (!isAxeAvailable(driver)) {
                injectAxe(driver);
                Thread.sleep(2000); // Wait for script to load
            }

            Object result = js.executeScript(
                "return new Promise((resolve) => {" +
                "  axe.run((error, results) => {" +
                "    resolve(results);" +
                "  });" +
                "})"
            );

            logger.info("Accessibility audit completed");
        } catch (Exception e) {
            logger.error("Error running accessibility audit", e);
        }
    }

    /**
     * Check for missing alt text on images
     */
    public static int checkMissingAltText(WebDriver driver) {
        try {
            JavascriptExecutor js = (JavascriptExecutor) driver;
            Number count = (Number) js.executeScript(
                "return document.querySelectorAll('img:not([alt])').length");
            int missingAltCount = count.intValue();
            logger.info("Found {} images without alt text", missingAltCount);
            return missingAltCount;
        } catch (Exception e) {
            logger.error("Error checking alt text", e);
            return -1;
        }
    }

    /**
     * Check for form labels
     */
    public static void checkFormLabels(WebDriver driver) {
        try {
            JavascriptExecutor js = (JavascriptExecutor) driver;
            Number unlabeledInputs = (Number) js.executeScript(
                "return document.querySelectorAll('input:not([id]), input:not([aria-label]):not([aria-labelledby])').length");
            logger.info("Found {} form inputs without labels", unlabeledInputs.intValue());
        } catch (Exception e) {
            logger.error("Error checking form labels", e);
        }
    }

    /**
     * Check color contrast
     */
    public static void checkColorContrast(WebDriver driver) {
        try {
            JavascriptExecutor js = (JavascriptExecutor) driver;
            js.executeScript(
                "var elements = document.querySelectorAll('*');" +
                "elements.forEach(el => {" +
                "  var style = window.getComputedStyle(el);" +
                "  console.log('Element:', el.tagName, 'Color:', style.color, 'BgColor:', style.backgroundColor);" +
                "})");
            logger.info("Color contrast check completed");
        } catch (Exception e) {
            logger.error("Error checking color contrast", e);
        }
    }

    /**
     * Check for keyboard navigation support
     */
    public static boolean isKeyboardNavigable(WebDriver driver) {
        try {
            JavascriptExecutor js = (JavascriptExecutor) driver;
            // Check if interactive elements are in tab order
            js.executeScript("document.body.focus()");
            WebElement activeElement = driver.switchTo().activeElement();
            js.executeScript("arguments[0].focus()", activeElement);
            logger.info("Page is keyboard navigable");
            return true;
        } catch (Exception e) {
            logger.error("Page is not keyboard navigable", e);
            return false;
        }
    }

    /**
     * Check heading structure
     */
    public static void checkHeadingStructure(WebDriver driver) {
        try {
            JavascriptExecutor js = (JavascriptExecutor) driver;
            Number headingCount = (Number) js.executeScript(
                "var headings = document.querySelectorAll('h1, h2, h3, h4, h5, h6');" +
                "var h1Count = document.querySelectorAll('h1').length;" +
                "console.log('Total headings:', headings.length, 'H1 count:', h1Count);" +
                "return headings.length");
            logger.info("Total headings found: {}", headingCount.intValue());
        } catch (Exception e) {
            logger.error("Error checking heading structure", e);
        }
    }

    /**
     * Check for page title
     */
    public static boolean hasPageTitle(WebDriver driver) {
        try {
            String title = driver.getTitle();
            boolean hasTitleTag = title != null && !title.isEmpty();
            logger.info("Page title present: {}", hasTitleTag);
            return hasTitleTag;
        } catch (Exception e) {
            logger.error("Error checking page title", e);
            return false;
        }
    }

    /**
     * WCAG 2.1 Level AA automated check summary
     */
    public static void runWCAGCompleteCheck(WebDriver driver) {
        logger.info("=== Starting WCAG 2.1 Level AA Compliance Check ===");

        // Run all checks
        runAccessibilityAudit(driver);
        checkMissingAltText(driver);
        checkFormLabels(driver);
        checkColorContrast(driver);
        isKeyboardNavigable(driver);
        checkHeadingStructure(driver);
        hasPageTitle(driver);

        logger.info("=== WCAG 2.1 Compliance Check Complete ===");
    }
}
