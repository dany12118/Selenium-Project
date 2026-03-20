# Allure Report Integration Guide

## Overview
This framework integrates **Allure Report**, a powerful test reporting tool that provides:
- 📊 Beautiful visual test reports
- 📈 Test history and trends
- 🎯 Test categorization (Features, Stories, Severity)
- 📸 Screenshots and attachments
- 📝 Detailed steps and logs
- ⚡ Real-time report generation

## Current Integration Status
✅ **Allure Dependencies Added** - allure-testng:2.21.0, allure-java-commons
✅ **Test Annotations Added** - @Feature, @Story, @Severity, @Description
✅ **Allure Results Generated** - Results stored in `target/allure-results/`
⏳ **Report Generation** - Ready for command-line generation

## How Allure Works in This Framework

### 1. **Test Execution Flow**
```
mvn clean test
    ↓
Tests execute with Allure listeners
    ↓
Results saved as JSON in target/allure-results/
    ↓
Generate HTML report from JSON results
```

### 2. **Test Annotations Used**

All test classes use Allure annotations for rich reporting:

```java
@Feature("Google Search Portal")  // Feature category
@Story("User navigates to Google") // User story
@Severity(SeverityLevel.BLOCKER)   // Test severity
@Description("Verify page loads")  // Test description
public void testGooglePageLoads() {
    AllureReportManager.logStep("Navigate to Google");
    // Test code...
    AllureReportManager.setParameter("BrowserTitle", driver.getTitle());
}
```

### 3. **Allure Step Logging**
Tests automatically log steps using `AllureReportManager`:
- `AllureReportManager.logStep(stepName)` - Log individual steps
- `AllureReportManager.setParameter(name, value)` - Add test parameters
- `AllureReportManager.attachScreenshot(path)` - Attach screenshots
- `AllureReportManager.attachFile(name, path, mimeType)` - Attach files

## Generate Allure Report

### Option 1: Using Allure CLI (Recommended)
**Prerequisites:** Install Allure CLI
```bash
# Download from: https://docs.qameta.io/allure/

# Generate report
allure generate target/allure-results/ -o target/allure-report/ --clean

# Open report in browser
allure open target/allure-report/
```

### Option 2: Using Maven Plugin
```bash
# Add to pom.xml reporting section (already configured)
# Then run:
mvn allure:serve
```

### Option 3: Manual Generation (No Allure CLI needed)
The Allure results are JSON files in `target/allure-results/`. You can:
1. Copy to any Allure report server
2. Use Allure CI plugins (Jenkins, GitLab CI, GitHub Actions)
3. View raw JSON for debugging

## Generated Allure Results

**Location:** `target/allure-results/`

**File Types:**
- `*-result.json` - Individual test results with status, steps, parameters
- `*-container.json` - Test suites and grouping information
- `environment.properties` - Environment information

**Content Example (result.json):**
```json
{
  "name": "testGooglePageLoads",
  "status": "passed",
  "stage": "finished",
  "steps": [
    {
      "name": "Navigate to Google homepage",
      "status": "passed"
    },
    {
      "name": "Verify page title contains 'Google'",
      "status": "passed"
    }
  ],
  "parameters": [
    {"name": "BrowserTitle", "value": "Google"},
    {"name": "PageURL", "value": "https://www.google.com"}
  ],
  "labels": [
    {"name": "feature", "value": "Google Search Portal"},
    {"name": "story", "value": "User navigates to Google home page"},
    {"name": "severity", "value": "blocker"}
  ]
}
```

## Test Execution Results

### Latest Test Run
- **Tests Run:** 5
- **Passed:** 4 ✅
- **Failed:** 1 ❌
- **Duration:** ~37 seconds

### Test Details with Allure

#### 1. testGooglePageLoads
- **Status:** ✅ PASSED
- **Feature:** Google Search Portal
- **Story:** User navigates to Google home page
- **Severity:** BLOCKER
- **Duration:** 4.257s
- **Steps:** 2 steps logged
- **Parameters:** BrowserTitle, PageURL

#### 2. testGoogleSearch
- **Status:** ✅ PASSED
- **Feature:** Google Search Portal
- **Story:** User performs search on Google
- **Severity:** CRITICAL
- **Duration:** 5.237s
- **Steps:** 2 steps logged
- **Parameters:** SearchStatus

#### 3. testIntentionalFailure
- **Status:** ❌ FAILED
- **Feature:** Google Search Portal
- **Story:** Test failure demonstration
- **Severity:** NORMAL
- **Duration:** 4.465s
- **Error:** AssertionError - Expected Yahoo page title but got: Google
- **Screenshot:** testIntentionalFailure_FAILED_20260312_121617_641.png

#### 4. testLoginPageLoads
- **Status:** ✅ PASSED
- **Feature:** OrangeHRM Authentication
- **Story:** User navigates to OrangeHRM login page
- **Severity:** BLOCKER
- **Duration:** 5.769s
- **Steps:** 2 steps logged
- **Parameters:** LoginPageURL, PageTitle

#### 5. testLoginWithValidCredentials
- **Status:** ✅ PASSED
- **Feature:** OrangeHRM Authentication
- **Story:** User logs in with valid credentials
- **Severity:** CRITICAL
- **Duration:** 5.366s
- **Steps:** 2 steps logged
- **Parameters:** LoginAttempt, Status

## Framework Integration Points

### 1. **BaseTest.java**
- Automatically injects test method name
- Integrates with MetricsCollector and ExtentReportManager
- Captures screenshots on failure

### 2. **AllureReportManager.java**
- Handles all Allure interactions
- Logs steps, parameters, attachments
- Thread-safe for parallel execution

### 3. **Test Classes**
- Use `@Feature`, `@Story`, `@Severity`, `@Description` annotations
- Call `AllureReportManager.logStep()` for detailed steps
- Set parameters with `AllureReportManager.setParameter()`

### 4. **pom.xml**
- Configured with allure-testng dependency
- Maven Surefire outputs to `target/allure-results/`
- Allure Maven plugin ready for report generation

## Usage Example

```java
@Feature("Login Feature")
@Story("Admin login process")
@Severity(SeverityLevel.BLOCKER)
@Test(description = "Admin can login with valid credentials")
public void testAdminLogin() {
    // Step 1
    AllureReportManager.logStep("Navigate to login page");
    loginPage.navigateTo();
    
    // Step 2
    AllureReportManager.logStep("Enter admin credentials");
    loginPage.enterUsername("admin");
    loginPage.enterPassword("password");
    AllureReportManager.setParameter("Username", "admin");
    
    // Step 3
    AllureReportManager.logStep("Click login button");
    loginPage.clickLogin();
    
    // Verify
    Assert.assertTrue(homePage.isDisplayed(), "Login failed");
    AllureReportManager.setParameter("LoginStatus", "Success");
}
```

## Report Visualization Features

When Allure report is generated, you get:

### Dashboard
- Total test count
- Pass/Fail ratio
- Duration trends
- Latest test runs

### Test Results
- Test name, status, duration
- Assigned feature and story
- Severity level
- Detailed steps with timing
- Parameters and artifacts

### History & Trends
- Test execution history
- Pass rate trends
- Flakiness detection
- Performance trends

### Features & Stories
- Organized by @Feature annotation
- Test mapping to user stories
- Feature-wise pass/fail rate

## Next Steps for Full Allure Integration

### 1. **Install Allure CLI** (Optional but Recommended)
```bash
# Windows: Download from https://docs.qameta.io/allure/
# Or use Scoop: scoop install allure
# Mac: brew install allure
# Linux: apt-get install allure
```

### 2. **Generate Report Locally**
```bash
mvn clean test
allure generate target/allure-results/ -o target/allure-report/ --clean
allure open target/allure-report/
```

### 3. **CI/CD Integration**
- GitLab CI: Use Allure plugin in `.gitlab-ci.yml`
- GitHub Actions: Use Allure Report action
- Jenkins: Configure Allure Publisher plugin

### 4. **Enhance Test Cases**
- Add more @Feature and @Story annotations
- Log critical steps with `AllureReportManager.logStep()`
- Set meaningful parameters with `setParameter()`
- Attach relevant files/screenshots

## Troubleshooting

### Q: Allure results not generating?
**A:** Ensure:
- `allure-testng` dependency is in pom.xml ✓
- Maven Surefire plugin has `systemPropertyVariables` configured ✓
- Tests are actually running (not skipped)

### Q: How to open Allure report without CLI?
**A:** 
- Allure results are JSON files in `target/allure-results/`
- Can be viewed by online Allure viewer
- Or copied to any web server with Allure installed

### Q: Can I use Allure with Extent Reports together?
**A:** Yes! Both work in parallel:
- Extent Report: Beautiful HTML UI (already embedded in report)
- Allure Reports: Detailed JSON results with advanced filtering
- Screenshots attached to both!

## Repository Structure

```
selenium-project/
├── target/
│   ├── allure-results/          ← Allure JSON results
│   │   ├── *-result.json
│   │   └── *-container.json
│   ├── allure-report/           ← Generated HTML report (after generation)
│   ├── screenshots/             ← Failed test screenshots
│   └── reports/                 ← Extent Reports
├── src/test/resources/
│   └── allure.properties        ← Allure configuration
├── src/test/java/com/example/
│   ├── tests/                   ← Test classes with Allure annotations
│   └── framework/reports/
│       ├── AllureReportManager.java
│       ├── ExtentReportManager.java
│       └── ...
└── pom.xml                      ← Allure dependencies configured
```

## Commands Reference

```bash
# Run tests and generate Allure results
mvn clean test

# Generate Allure HTML report (requires Allure CLI)
allure generate target/allure-results/ -o target/allure-report/ --clean

# Open report in default browser
allure open target/allure-report/

# Clean Allure results
rm -rf target/allure-results/ target/allure-report/

# View Allure results in CLI
allure generate target/allure-results/ --clean -q && allure open
```

---

**Status:** ✅ Allure fully integrated and generating results!

For more details: [Allure Official Docs](https://docs.qameta.io/allure/)
