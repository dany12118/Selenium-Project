# Framework API Documentation

## Overview

This Selenium testing framework provides a comprehensive suite of utilities and base classes for building scalable, maintainable test automation. It supports UI testing, API testing, database operations, performance tracking, and accessibility compliance.

## Core Components

### 1. Driver Management

**DriverManager** - Multi-browser thread-safe driver management

```java
// Initialize driver
WebDriver driver = DriverManager.getCurrentDriver();

// Quit driver
DriverManager.quitDriver();
```

**Features**:
- Thread-safe operations with ThreadLocal
- Multi-browser support (Chrome, Firefox, Edge, Safari)
- Automatic WebDriver binary management via WebDriverManager
- Configurable timeouts and browser options

### 2. Configuration Management

**EnvironmentConfig** - Multi-environment configuration with secrets support

```java
// Get basic property
String baseUrl = EnvironmentConfig.getProperty("baseUrl");

// Get with default value
String browser = EnvironmentConfig.getProperty("browser", "chrome");

// Get as integer
int timeout = EnvironmentConfig.getPropertyAsInt("implicitWait", 10);

// Get as boolean
boolean headless = EnvironmentConfig.getPropertyAsBoolean("headless", false);

// Get active environment
String env = EnvironmentConfig.getActiveEnvironment(); // dev, staging, prod
```

**Priority Order**:
1. Environment Variables
2. System Properties
3. application-{env}.properties
4. config.properties

### 3. Page Object Model

**BasePage** - Base class for all page objects

```java
public class LoginPage extends BasePage {
    @FindBy(id = "username")
    private WebElement usernameField;
    
    @FindBy(id = "password")
    private WebElement passwordField;
    
    @FindBy(xpath = "//button[contains(text(), 'Login')]")
    private WebElement loginButton;
    
    public LoginPage(WebDriver driver) {
        super(driver);
    }
    
    public void enterUsername(String username) {
        sendKeys(usernameField, username);
    }
    
    public void enterPassword(String password) {
        sendKeys(passwordField, password);
    }
    
    public DashboardPage clickLoginButton() {
        click(loginButton);
        return new DashboardPage(driver);
    }
}
```

**Available Methods**:
- `click(WebElement element)` - Click with wait
- `sendKeys(WebElement element, String text)` - Type text
- `getText(WebElement element)` - Get element text
- `isDisplayed(WebElement element)` - Check visibility
- `getAttribute(WebElement element, String attribute)` - Get attribute value

### 4. Wait Utilities

**WaitUtils** - Comprehensive explicit wait conditions

```java
// Wait for element visibility
WaitUtils.waitForElementVisible(driver, element, 20);

// Wait for element clickability
WaitUtils.waitForElementClickable(driver, element, 20);

// Wait for element presence
WaitUtils.waitForElementPresent(driver, locator, 20);

// Wait for text in element
WaitUtils.waitForTextToBePresent(driver, element, "Expected Text");

// Wait for URL to contain
WaitUtils.waitForUrlToContain(driver, "expectedUrl");

// Wait for element invisibility
WaitUtils.waitForElementInvisible(driver, element, 20);
```

### 5. Exception Hierarchy

Custom exceptions for better error handling:

```java
try {
    // Framework operations
} catch (WaitException e) {
    // Element not found or timeout
} catch (PageException e) {
    // Page object operation failed
} catch (ConfigException e) {
    // Configuration error
} catch (FrameworkException e) {
    // General framework error
}
```

### 6. API Testing

**ApiClient** - REST client wrapper using RestAssured

```java
ApiClient apiClient = new ApiClient("https://api.example.com");

// GET Request
Response getResponse = apiClient
    .header("Authorization", "Bearer token")
    .get("/users");

// POST Request
Response postResponse = apiClient
    .reset()
    .header("Content-Type", "application/json")
    .body("{\"name\": \"John\"}")
    .post("/users");

// Assertions
int statusCode = getResponse.getStatusCode();
String responseBody = getResponse.getBody().asString();
```

### 7. Database Operations

**DatabaseHelper** - JDBC wrapper with connection pooling

```java
DatabaseHelper db = new DatabaseHelper();
db.connect("jdbc:mysql://localhost:3306/testdb", "user", "password");

// Execute SELECT query
List<Map<String, Object>> results = db.executeSelectQuery(
    "SELECT * FROM users WHERE username = 'admin'");

// Execute UPDATE/INSERT/DELETE
int rowsAffected = db.executeUpdateQuery(
    "UPDATE users SET status = 'active' WHERE id = 1");

// Check if record exists
boolean exists = db.isRecordExists("SELECT id FROM users WHERE id = 1");

// Disconnect
db.disconnect();
```

### 8. Test Data Factory

**TestDataFactory** - Builder pattern for test data creation

```java
// Create user with builder pattern
TestUser user = TestDataFactory.createUser()
    .withUsername("testuser")
    .withPassword("Test@123")
    .withEmail("test@example.com")
    .withFirstName("Test")
    .withLastName("User")
    .build();

// Use default test users
TestUser adminUser = TestDataFactory.createDefaultAdminUser();
TestUser regularUser = TestDataFactory.createDefaultRegularUser();

// Create products
TestProduct product = TestDataFactory.createProduct()
    .withProductId("PROD001")
    .withProductName("Test Product")
    .withPrice(99.99)
    .withQuantity(10)
    .build();
```

### 9. Metrics Collection

**MetricsCollector** - Performance and execution tracking

```java
// Start metrics for test
MetricsCollector.startMetrics("testLoginWithValidCredentials");

// Record test completion
MetricsCollector.recordTestCompletion("PASS");

// Record error
MetricsCollector.recordError("Login failed", stackTrace);

// Check if test is slow
if (MetricsCollector.isSlowTest(5000)) {
    logger.warn("Test took longer than 5000ms");
}

// Export metrics
MetricsCollector.exportMetricsToJson("target/metrics/test_metrics.json");
```

### 10. Performance Utilities

**PerformanceUtils** - Performance measurement and baseline comparison

```java
// Measure operation time
PerformanceUtils.startTimer();
// ... perform operation ...
long duration = PerformanceUtils.stopTimer("Login Operation");

// Assert response time within threshold
PerformanceUtils.assertResponseTime("API Call", actualTime, 3000);

// Compare against baseline
PerformanceUtils.compareAgainstBaseline("Page Load", actualTime, baselineTime);

// Measure page load time
PerformanceUtils.assertPageLoadTime(driver, 5000);
```

### 11. Accessibility Testing

**AccessibilityUtils** - WCAG 2.1 compliance checking

```java
// Run complete accessibility audit
AccessibilityUtils.runWCAGCompleteCheck(driver);

// Check for missing alt text
int missingAltCount = AccessibilityUtils.checkMissingAltText(driver);

// Check form labels
AccessibilityUtils.checkFormLabels(driver);

// Check keyboard navigation
boolean isKeyboardNavigable = AccessibilityUtils.isKeyboardNavigable(driver);

// Check page title
boolean hasTitle = AccessibilityUtils.hasPageTitle(driver);
```

### 12. Video Recording

**VideoRecorder** - Auto record test execution on failure

```java
VideoRecorder recorder = new VideoRecorder();

// Start recording
recorder.startRecording("testLoginWithValidCredentials");

// ... run test ...

// Stop recording and get file path
String videoFile = recorder.stopRecording();

// Check if FFmpeg available
if (VideoRecorder.isFfmpegAvailable()) {
    // Recording will be created
}
```

### 13. Test Listeners

**TestListener** - Test lifecycle management

Automatically handles:
- Test start logging
- Screenshot capture on failure
- ExtentReports logging
- Allure Reports integration
- Metrics collection
- Video recording on failure
- Retry logic

## Common Usage Patterns

### Pattern 1: Basic Test with Page Objects

```java
@Test(description = "Verify user login", groups = {"smoke"})
public void testUserLogin() {
    // Arrange
    String username = "admin";
    String password = "admin123";
    
    // Act
    LoginPage loginPage = new LoginPage(driver);
    DashboardPage dashboardPage = loginPage
        .enterUsername(username)
        .enterPassword(password)
        .clickLoginButton();
    
    // Assert
    Assert.assertTrue(dashboardPage.isDashboardDisplayed(), 
        "Dashboard should be visible after login");
}
```

### Pattern 2: Multi-Environment Test

```java
@Test(description = "Verify API call",groups = {"regression"})
public void testApiCall() {
    // Configuration automatically loads for active environment
    String baseUrl = EnvironmentConfig.getProperty("baseUrl");
    String apiKey = EnvironmentConfig.getProperty("api.key");
    
    ApiClient client = new ApiClient(baseUrl);
    Response response = client
        .header("Authorization", "Bearer " + apiKey)
        .get("/api/users");
    
    Assert.assertEquals(response.getStatusCode(), 200);
}
```

### Pattern 3: Data-Driven Test

```java
@DataProvider(name = "loginTestData")
public Object[][] getLoginTestData() {
    return new Object[][] {
        {TestDataFactory.createDefaultAdminUser()},
        {TestDataFactory.createDefaultRegularUser()},
        {TestDataFactory.createInvalidUser()}
    };
}

@Test(dataProvider = "loginTestData", groups = {"regression"})
public void testLoginWithMultipleUsers(TestDataFactory.TestUser user) {
    LoginPage loginPage = new LoginPage(driver);
    loginPage.login(user.username, user.password);
    // ... assertions ...
}
```

## Advanced Features

### Custom Wait Conditions

Extend WaitUtils to add custom conditions specific to your application.

### Retry Mechanism

Tests can be decorated with @Retry annotation or use TestRetryListener for automatic retries on failure.

### Parallel Execution

TestNG supports parallel test execution configured in testng.xml:
```xml
<suite parallel="methods" thread-count="4">
```

## Running Tests

### Maven Commands

```bash
# Run all tests
mvn clean test

# Run specific group
mvn clean test -Dgroups=smoke

# Run in specific environment
ENVIRONMENT=staging mvn clean test

# Generate reports
mvn clean test
# Reports: target/reports/TestReport_*.html
#          target/surefire-reports/index.html
```

## Reporting

### ExtentReports
- Automatically generated after test execution
- Location: `target/reports/TestReport_*.html`
- Includes screenshots on failure

### TestNG Reports
- Location: `target/surefire-reports/`
- Provides test execution summary

### Metrics Export
- JSON export to `target/metrics/`
- Includes execution time, pass rate, flakiness data

## Troubleshooting

| Issue | Solution |
|-------|----------|
| "Element not found" | Check XPath/CSS selector, verify element is visible |
| "StaleElementReferenceException" | Re-locate element after page refresh |
| "TimeoutException" | Increase wait timeout or checkpage load logic |
| "WebDriver not initialized" | Ensure DriverManager is initialized before tests |
| "Configuration not found" | Check app- lication-{env}.properties exists |

## Support & Contribution

For issues, feature requests, or contributions, see [CONTRIBUTING.md](CONTRIBUTING.md)
