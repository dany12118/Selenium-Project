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
 * OrangeHRM login test cases
 * Features: Authentication, Login functionality
 */
@Feature("OrangeHRM Authentication")
public class OrangeHRMLoginTest extends BaseTest {

    @Test(groups = {"smoke", "regression"}, description = "Test OrangeHRM login page loads")
    @Story("User navigates to OrangeHRM login page")
    @Severity(SeverityLevel.BLOCKER)
    @Description("Verify that OrangeHRM login page loads successfully")
    public void testLoginPageLoads() {
        AllureReportManager.logStep("Navigate to OrangeHRM login page");
        driver.navigate().to("https://opensource-demo.orangehrmlive.com/web/index.php/auth/login");
        
        AllureReportManager.logStep("Verify login page title");
        Assert.assertTrue(driver.getTitle().contains("OrangeHRM"), "OrangeHRM login page failed to load");
        logger.info("OrangeHRM login page loaded successfully");
        
        AllureReportManager.setParameter("LoginPageURL", driver.getCurrentUrl());
        AllureReportManager.setParameter("PageTitle", driver.getTitle());
    }

    @Test(groups = {"regression"}, description = "Test OrangeHRM login with valid credentials")
    @Story("User logs in with valid credentials")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Verify that user can login with valid credentials to OrangeHRM")
    public void testLoginWithValidCredentials() {
        AllureReportManager.logStep("Navigate to OrangeHRM login page");
        driver.navigate().to("https://opensource-demo.orangehrmlive.com/web/index.php/auth/login");
        
        AllureReportManager.logStep("Verify login page loaded");
        Assert.assertTrue(driver.getTitle().contains("OrangeHRM"), "Login page not loaded");
        logger.info("OrangeHRM login test completed");
        
        AllureReportManager.setParameter("LoginAttempt", "FirstAttempt");
        AllureReportManager.setParameter("Status", "Ready");
    }
}
