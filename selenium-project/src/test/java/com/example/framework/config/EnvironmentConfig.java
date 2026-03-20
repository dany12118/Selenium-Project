package com.example.framework.config;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.example.framework.exceptions.ConfigException;

/**
 * Enterprise configuration manager supporting multiple environments
 * Supports overrides via environment variables
 * Priority: Environment Variables > Active Profile > Default properties
 */
public class EnvironmentConfig {
    private static final Logger logger = LogManager.getLogger(EnvironmentConfig.class);
    private static Properties properties = new Properties();
    private static String activeEnvironment;

    static {
        loadConfiguration();
    }

    /**
     * Load configuration based on active environment
     * Environment can be set via:
     * 1. ENVIRONMENT environment variable
     * 2. System property: environment
     * 3. Default: "dev"
     */
    private static void loadConfiguration() {
        // Determine active environment
        activeEnvironment = System.getenv("ENVIRONMENT");
        if (activeEnvironment == null) {
            activeEnvironment = System.getProperty("environment", "dev");
        }
        logger.info("Loading configuration for environment: {}", activeEnvironment);

        // Load default properties first
        loadPropertiesFile("src/test/resources/config/config.properties");

        // Load environment-specific properties
        String envPropertiesFile = String.format("src/test/resources/application-%s.properties", activeEnvironment);
        loadPropertiesFile(envPropertiesFile);
    }

    /**
     * Load properties from file
     */
    private static void loadPropertiesFile(String filePath) {
        try {
            File file = new File(filePath);
            if (file.exists()) {
                try (FileInputStream fis = new FileInputStream(file)) {
                    properties.load(fis);
                    logger.info("Loaded properties from: {}", filePath);
                }
            } else {
                logger.warn("Properties file not found: {}", filePath);
            }
        } catch (IOException e) {
            throw new ConfigException("Failed to load properties from " + filePath, e);
        }
    }

    /**
     * Get property value with environment variable override support
     * @param key Property key
     * @return Property value or null if not found
     */
    public static String getProperty(String key) {
        // Priority 1: Environment variable
        String envValue = System.getenv(key);
        if (envValue != null) {
            logger.debug("Using environment variable for {}: {}", key, maskSensitiveValue(key, envValue));
            return envValue;
        }

        // Priority 2: System property
        String sysValue = System.getProperty(key);
        if (sysValue != null) {
            logger.debug("Using system property for {}: {}", key, maskSensitiveValue(key, sysValue));
            return sysValue;
        }

        // Priority 3: Properties file
        String propValue = properties.getProperty(key);
        if (propValue != null) {
            logger.debug("Using property file value for {}: {}", key, maskSensitiveValue(key, propValue));
        }
        return propValue;
    }

    /**
     * Get property with default value
     */
    public static String getProperty(String key, String defaultValue) {
        String value = getProperty(key);
        return value != null ? value : defaultValue;
    }

    /**
     * Get property as integer
     */
    public static int getPropertyAsInt(String key, int defaultValue) {
        String value = getProperty(key);
        if (value != null && !value.isEmpty()) {
            try {
                return Integer.parseInt(value);
            } catch (NumberFormatException e) {
                logger.warn("Invalid integer value for {}: {}", key, value);
                return defaultValue;
            }
        }
        return defaultValue;
    }

    /**
     * Get property as boolean
     */
    public static boolean getPropertyAsBoolean(String key, boolean defaultValue) {
        String value = getProperty(key);
        if (value != null && !value.isEmpty()) {
            return Boolean.parseBoolean(value);
        }
        return defaultValue;
    }

    /**
     * Get active environment
     */
    public static String getActiveEnvironment() {
        return activeEnvironment;
    }

    /**
     * Mask sensitive values in logs (passwords, tokens, etc.)
     */
    private static String maskSensitiveValue(String key, String value) {
        if (key.toLowerCase().contains("password") ||
            key.toLowerCase().contains("token") ||
            key.toLowerCase().contains("secret") ||
            key.toLowerCase().contains("key")) {
            return "***MASKED***";
        }
        return value;
    }

    /**
     * Reload configuration
     */
    public static void reload() {
        properties.clear();
        loadConfiguration();
        logger.info("Configuration reloaded");
    }
}
