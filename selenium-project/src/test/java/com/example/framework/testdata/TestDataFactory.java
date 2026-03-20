package com.example.framework.testdata;

/**
 * Test data factory for building test objects
 * Uses builder pattern for flexible test data creation
 */
public class TestDataFactory {

    /**
     * User data builder
     */
    public static class UserBuilder {
        private String username;
        private String password;
        private String email;
        private String firstName;
        private String lastName;
        private String department;
        private boolean isActive = true;

        public UserBuilder withUsername(String username) {
            this.username = username;
            return this;
        }

        public UserBuilder withPassword(String password) {
            this.password = password;
            return this;
        }

        public UserBuilder withEmail(String email) {
            this.email = email;
            return this;
        }

        public UserBuilder withFirstName(String firstName) {
            this.firstName = firstName;
            return this;
        }

        public UserBuilder withLastName(String lastName) {
            this.lastName = lastName;
            return this;
        }

        public UserBuilder withDepartment(String department) {
            this.department = department;
            return this;
        }

        public UserBuilder asInactive() {
            this.isActive = false;
            return this;
        }

        public TestUser build() {
            return new TestUser(username, password, email, firstName, lastName, department, isActive);
        }
    }

    /**
     * Test user data class
     */
    public static class TestUser {
        public final String username;
        public final String password;
        public final String email;
        public final String firstName;
        public final String lastName;
        public final String department;
        public final boolean isActive;

        public TestUser(String username, String password, String email,
                       String firstName, String lastName, String department, boolean isActive) {
            this.username = username;
            this.password = password;
            this.email = email;
            this.firstName = firstName;
            this.lastName = lastName;
            this.department = department;
            this.isActive = isActive;
        }

        @Override
        public String toString() {
            return String.format("TestUser{username='%s', email='%s'}", username, email);
        }
    }

    /**
     * Create user builder
     */
    public static UserBuilder createUser() {
        return new UserBuilder();
    }

    /**
     * Create default test admin user
     */
    public static TestUser createDefaultAdminUser() {
        return new UserBuilder()
            .withUsername("admin")
            .withPassword("admin123")
            .withEmail("admin@test.com")
            .withFirstName("Admin")
            .withLastName("User")
            .withDepartment("IT")
            .build();
    }

    /**
     * Create default test regular user
     */
    public static TestUser createDefaultRegularUser() {
        return new UserBuilder()
            .withUsername("testuser")
            .withPassword("Test@123")
            .withEmail("testuser@test.com")
            .withFirstName("Test")
            .withLastName("User")
            .withDepartment("Operations")
            .build();
    }

    /**
     * Create invalid user for negative tests
     */
    public static TestUser createInvalidUser() {
        return new UserBuilder()
            .withUsername("")
            .withPassword("")
            .withEmail("invalid")
            .build();
    }

    /**
     * Product data builder
     */
    public static class ProductBuilder {
        private String productId;
        private String productName;
        private double price;
        private int quantity;
        private String category;
        private String description;
        private boolean inStock = true;

        public ProductBuilder withProductId(String productId) {
            this.productId = productId;
            return this;
        }

        public ProductBuilder withProductName(String productName) {
            this.productName = productName;
            return this;
        }

        public ProductBuilder withPrice(double price) {
            this.price = price;
            return this;
        }

        public ProductBuilder withQuantity(int quantity) {
            this.quantity = quantity;
            return this;
        }

        public ProductBuilder withCategory(String category) {
            this.category = category;
            return this;
        }

        public ProductBuilder withDescription(String description) {
            this.description = description;
            return this;
        }

        public ProductBuilder asOutOfStock() {
            this.inStock = false;
            return this;
        }

        public TestProduct build() {
            return new TestProduct(productId, productName, price, quantity, category, description, inStock);
        }
    }

    /**
     * Test product data class
     */
    public static class TestProduct {
        public final String productId;
        public final String productName;
        public final double price;
        public final int quantity;
        public final String category;
        public final String description;
        public final boolean inStock;

        public TestProduct(String productId, String productName, double price, int quantity,
                          String category, String description, boolean inStock) {
            this.productId = productId;
            this.productName = productName;
            this.price = price;
            this.quantity = quantity;
            this.category = category;
            this.description = description;
            this.inStock = inStock;
        }

        @Override
        public String toString() {
            return String.format("TestProduct{id='%s', name='%s', price=%.2f}", productId, productName, price);
        }
    }

    /**
     * Create product builder
     */
    public static ProductBuilder createProduct() {
        return new ProductBuilder();
    }
}
