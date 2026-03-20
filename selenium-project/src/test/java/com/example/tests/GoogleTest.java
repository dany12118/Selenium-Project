package com.example.tests;

import org.testng.Assert;
import org.testng.annotations.Test;
import com.example.framework.base.BaseTest;
import com.example.framework.reports.AllureReportManager;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.Description;

/**
 * Google test cases
 * Features: Web Navigation, Search functionality
 */
@Feature("Google Search Portal")
public class GoogleTest extends BaseTest {

    @Test(groups = {"smoke", "regression"}, description = "Test Google page loads successfully")
    @Story("User navigates to Google home page")
    @Severity(SeverityLevel.BLOCKER)
    @Description("Verify that Google homepage loads successfully and displays correct title")
    public void testGooglePageLoads() {
        AllureReportManager.logStep("Navigate to Google homepage");
        driver.navigate().to("https://www.google.com");
        
        AllureReportManager.logStep("Verify page title contains 'Google'");
        Assert.assertTrue(driver.getTitle().contains("Google"), "Google page failed to load");
        logger.info("Google page loaded successfully");
        
        AllureReportManager.setParameter("BrowserTitle", driver.getTitle());
        AllureReportManager.setParameter("PageURL", driver.getCurrentUrl());
    }

    @Test(groups = {"regression"}, description = "Test Google search functionality")
    @Story("User performs search on Google")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Verify that user can access Google search page")
    public void testGoogleSearch() {
        AllureReportManager.logStep("Navigate to Google search");
        driver.navigate().to("https://www.google.com");
        
        AllureReportManager.logStep("Verify search page loaded");
        Assert.assertTrue(driver.getTitle().contains("Google"), "Google search page not loaded");
        logger.info("Google search test completed");
        
        AllureReportManager.setParameter("SearchStatus", "Ready");
    }

    @Test(groups = {"regression"}, description = "Intentional failure to test reporting")
    @Story("Test failure demonstration")
    @Severity(SeverityLevel.NORMAL)
    @Description("This test is intentionally designed to fail to demonstrate failure reporting and screenshot capture")
    public void testIntentionalFailure() {
        AllureReportManager.logStep("Navigate to Google");
        driver.navigate().to("https://www.google.com");
        logger.info("Test started - this test will fail");
        
        AllureReportManager.logStep("Verify incorrect page title (Yahoo instead of Google)");
        // This assertion will fail - demonstrating failure reporting
        Assert.assertTrue(driver.getTitle().contains("Yahoo"), 
            "Expected Yahoo page title but got: " + driver.getTitle());
    }
}
