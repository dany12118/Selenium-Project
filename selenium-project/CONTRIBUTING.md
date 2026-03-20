# Contributing Guidelines

Welcome to the Selenium Test Framework! This document outlines how to contribute, write tests, and maintain code quality.

## Table of Contents
- [Test Writing Standards](#test-writing-standards)
- [Naming Conventions](#naming-conventions)
- [Code Style](#code-style)
- [Pull Request Process](#pull-request-process)
- [Best Practices](#best-practices)

## Test Writing Standards

### Test Anatomy
```java
@Test(description = "Verify login functionality", groups = {"smoke", "regression"})
public void testLoginWithValidCredentials() {
    // Arrange: Set up test data
    String username = "admin";
    String password = "admin123";
    
    // Act: Perform actions
    loginPage.enterUsername(username);
    loginPage.enterPassword(password);
    loginPage.clickLoginButton();
    
    // Assert: Verify results
    Assert.assertTrue(dashboardPage.isDashboardDisplayed(), "Dashboard should be visible");
}
```

### Test Grouping
- **smoke**: Critical tests that run on every build
- **regression**: Full regression suite
- **sanity**: Quick sanity checks
- **performance**: Performance-related tests
- **accessibility**: Accessibility compliance tests

## Naming Conventions

### Test Method Naming
Format: `test<Action><ExpectedResult>`

Examples:
- `testLoginWithValidCredentials()`
- `testSearchWithEmptyKeyword()`
- `testAddItemToCart()`
- `testDeleteUserAsNonAdmin()` - For negative tests

### Variable Naming
- Use descriptive names: `username`, `expectedErrorMessage` (not `u`, `msg`)
- Constants in UPPER_CASE: `ADMIN_USERNAME`, `BASE_URL`

### File Naming
- Test Classes: `*Test.java` (e.g., `LoginPageTest.java`)
- Page Objects: `*Page.java` (e.g., `LoginPage.java`)
- Utilities: `*Utils.java` (e.g., `WaitUtils.java`)

## Code Style

### Import Organization
1. java imports
2. Third-party imports
3. Framework imports

### Line Length
- Maximum 120 characters per line

### Indentation
- 4 spaces (no tabs)

### Comments
- Add meaningful comments for complex logic
- Use JavaDoc for public methods

```java
/**
 * Logs in with provided credentials
 * @param username User's login username
 * @param password User's login password
 * @throws FrameworkException if login fails
 */
public void login(String username, String password) {
    // Implementation
}
```

## Pull Request Process

1. **Create Feature Branch**
   ```
   git checkout -b feature/describe-your-feature
   ```

2. **Write Tests**
   - Add tests for new functionality
   - Ensure all tests pass locally
   - Run with multiple browsers if applicable

3. **Code Review Checklist**
   - [ ] Tests follow naming conventions
   - [ ] No hardcoded values (use properties)
   - [ ] Proper exception handling
   - [ ] Meaningful commit messages
   - [ ] Updated documentation

4. **Submit PR**
   - Reference related issues
   - Provide description of changes
   - Include test results

## Best Practices

### 1. Use Page Object Model
```java
// GOOD
GoogleHomePage page = new GoogleHomePage(driver);
page.searchFor("selenium");

// BAD
WebElement searchBox = driver.findElement(By.id("search"));
searchBox.sendKeys("selenium");
```

### 2. Use ConfigReader for Configuration
```java
// GOOD
String username = ConfigReader.getProperty("testuser.username");

// BAD
String username = "admin";
```

### 3. Use Explicit Waits
```java
// GOOD
WaitUtils.waitForElementVisible(driver, element, 20);

// BAD
Thread.sleep(5000);
```

### 4. Add Meaningful Assertions
```java
// GOOD
Assert.assertEquals(actualValue, expectedValue, 
    "User should be created successfully");

// BAD
Assert.assertEquals(actualValue, expectedValue);
```

### 5. Use SoftAssert for Multiple Validations
```java
SoftAssert softAssert = new SoftAssert();
softAssert.assertEquals(firstName, "John");
softAssert.assertEquals(lastName, "Doe");
softAssert.assertTrue(isActive);
softAssert.assertAll();
```

### 6. Proper Exception Handling
```java
try {
    // Code that might throw exception
} catch (StaleElementReferenceException e) {
    logger.error("Element became stale, retrying...", e);
    // Retry logic
} catch (TimeoutException e) {
    logger.error("Timeout waiting for element", e);
    throw new WaitException("Element not found within timeout", e);
}
```

## Running Tests

### Run All Tests
```bash
mvn clean test
```

### Run Specific Test Group
```bash
mvn clean test -Dgroups=smoke
```

### Run Specific Test Class
```bash
mvn clean test -Dtest=LoginPageTest
```

### Run Specific Test Method
```bash
mvn clean test -Dtest=LoginPageTest#testLoginWithValidCredentials
```

### Generate Reports
```bash
mvn clean test
# ExtentReports: target/reports/TestReport_*.html
# TestNG Report: target/surefire-reports/index.html
```

## Code Quality Standards

- **Minimum Test Coverage**: 80%
- **Max Cyclomatic Complexity**: 10
- **Code Duplication**: < 5%
- **Pass Rate**: 100% (no skipped tests without justification)

## Support

For questions or issues, please reach out to the QA team or create a GitHub issue.
