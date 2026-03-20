package com.example.framework.driver;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.safari.SafariDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxOptions;
import java.time.Duration;

/**
 * WebDriver management class
 * Handles creation, management, and disposal of WebDriver instances
 */
public class DriverManager {
    private static final Logger logger = LogManager.getLogger(DriverManager.class);
    private static final ThreadLocal<WebDriver> driverThreadLocal = new ThreadLocal<>();
    private static final String BROWSER = System.getProperty("browser", "chrome");
    private static Duration implicitWait = Duration.ofSeconds(0); // No implicit wait by default

    /**
     * Get WebDriver instance
     */
    public static WebDriver getDriver() {
        if (driverThreadLocal.get() == null) {
            initializeDriver();
        }
        return driverThreadLocal.get();
    }

    /**
     * Initialize WebDriver based on browser type
     */
    private static void initializeDriver() {
        String browser = BROWSER.toLowerCase();
        logger.info("Initializing WebDriver for browser: {}", browser);

        switch (browser) {
            case "firefox":
                WebDriverManager.firefoxdriver().setup();
                FirefoxOptions firefoxOptions = new FirefoxOptions();
                driverThreadLocal.set(new FirefoxDriver(firefoxOptions));
                break;

            case "edge":
                WebDriverManager.edgedriver().setup();
                driverThreadLocal.set(new EdgeDriver());
                break;

            case "safari":
                WebDriverManager.safaridriver().setup();
                driverThreadLocal.set(new SafariDriver());
                break;

            case "chrome":
            default:
                WebDriverManager.chromedriver().setup();
                ChromeOptions chromeOptions = new ChromeOptions();
                // Add headless option if needed
                // chromeOptions.addArguments("--headless");
                chromeOptions.addArguments("--no-sandbox");
                chromeOptions.addArguments("--disable-dev-shm-usage");
                driverThreadLocal.set(new ChromeDriver(chromeOptions));
                break;
        }

        WebDriver driver = driverThreadLocal.get();
        driver.manage().timeouts().implicitlyWait(implicitWait);
        driver.manage().window().maximize();
        logger.info("WebDriver initialized successfully for: {}", browser);
    }

    /**
     * Set implicit wait timeout
     */
    public static void waitForElementPresent(long seconds) {
        implicitWait = Duration.ofSeconds(seconds);
        WebDriver driver = driverThreadLocal.get();
        if (driver != null) {
            driver.manage().timeouts().implicitlyWait(implicitWait);
            logger.info("Implicit wait set to: {} seconds", seconds);
        }
    }

    /**
     * Quit WebDriver
     */
    public static void quitDriver() {
        WebDriver driver = driverThreadLocal.get();
        if (driver != null) {
            driver.quit();
            driverThreadLocal.remove();
            logger.info("WebDriver quit successfully");
        }
    }

    /**
     * Check if driver is initialized
     */
    public static boolean isDriverInitialized() {
        return driverThreadLocal.get() != null;
    }

    /**
     * Reset driver
     */
    public static void resetDriver() {
        quitDriver();
        driverThreadLocal.set(null);
    }
}
