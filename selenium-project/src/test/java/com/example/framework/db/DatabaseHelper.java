package com.example.framework.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.example.framework.exceptions.FrameworkException;

/**
 * Database utility class for managing database connections and queries
 * Supports MySQL, PostgreSQL, H2, and other JDBC-compliant databases
 */
public class DatabaseHelper {
    private static final Logger logger = LogManager.getLogger(DatabaseHelper.class);
    private Connection connection;
    private static final int MAX_RETRIES = 3;
    private static final long RETRY_DELAY_MS = 1000;

    /**
     * Initialize database connection
     * @param url Database URL (JDBC)
     * @param username Database username
     * @param password Database password
     */
    public void connect(String url, String username, String password) {
        int retries = 0;
        while (retries < MAX_RETRIES) {
            try {
                connection = DriverManager.getConnection(url, username, password);
                logger.info("Database connected successfully to: {}", url);
                return;
            } catch (Exception e) {
                retries++;
                logger.warn("Database connection failed (attempt {}/{}): {}", retries, MAX_RETRIES, e.getMessage());
                if (retries < MAX_RETRIES) {
                    try {
                        Thread.sleep(RETRY_DELAY_MS);
                    } catch (InterruptedException ie) {
                        Thread.currentThread().interrupt();
                    }
                } else {
                    throw new FrameworkException("Failed to connect to database after " + MAX_RETRIES + " attempts", e);
                }
            }
        }
    }

    /**
     * Execute SELECT query and return results
     * @param query SQL SELECT query
     * @return List of maps containing row data
     */
    public List<Map<String, Object>> executeSelectQuery(String query) {
        List<Map<String, Object>> results = new ArrayList<>();
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);

            while (resultSet.next()) {
                Map<String, Object> rowData = new HashMap<>();
                int columnCount = resultSet.getMetaData().getColumnCount();
                for (int i = 1; i <= columnCount; i++) {
                    String columnName = resultSet.getMetaData().getColumnName(i);
                    Object columnValue = resultSet.getObject(i);
                    rowData.put(columnName, columnValue);
                }
                results.add(rowData);
            }
            resultSet.close();
            statement.close();
            logger.info("Query executed successfully: {}", query);
        } catch (Exception e) {
            logger.error("Error executing query: {}", query, e);
            throw new FrameworkException("Database query execution failed", e);
        }
        return results;
    }

    /**
     * Execute UPDATE/INSERT/DELETE query
     * @param query SQL DML query
     * @return Number of rows affected
     */
    public int executeUpdateQuery(String query) {
        int rowsAffected = 0;
        try {
            Statement statement = connection.createStatement();
            rowsAffected = statement.executeUpdate(query);
            statement.close();
            logger.info("Update query executed successfully. Rows affected: {}", rowsAffected);
        } catch (Exception e) {
            logger.error("Error executing update query: {}", query, e);
            throw new FrameworkException("Database update query failed", e);
        }
        return rowsAffected;
    }

    /**
     * Execute batch of queries
     * @param queries List of SQL queries
     * @return Array of row counts for each query
     */
    public int[] executeBatch(List<String> queries) {
        int[] results = new int[0];
        try {
            Statement statement = connection.createStatement();
            for (String query : queries) {
                statement.addBatch(query);
            }
            results = statement.executeBatch();
            statement.close();
            logger.info("Batch executed successfully with {} queries", queries.size());
        } catch (Exception e) {
            logger.error("Error executing batch queries", e);
            throw new FrameworkException("Database batch execution failed", e);
        }
        return results;
    }

    /**
     * Check if record exists
     * @param query SQL SELECT query
     * @return true if record exists, false otherwise
     */
    public boolean isRecordExists(String query) {
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);
            boolean exists = resultSet.next();
            resultSet.close();
            statement.close();
            return exists;
        } catch (Exception e) {
            logger.error("Error checking record existence: {}", query, e);
            throw new FrameworkException("Record existence check failed", e);
        }
    }

    /**
     * Get single value from query
     * @param query SQL query returning single value
     * @return Single object value, null if no result
     */
    public Object getSingleValue(String query) {
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);
            Object value = null;
            if (resultSet.next()) {
                value = resultSet.getObject(1);
            }
            resultSet.close();
            statement.close();
            return value;
        } catch (Exception e) {
            logger.error("Error getting single value from query: {}", query, e);
            throw new FrameworkException("Failed to retrieve single value", e);
        }
    }

    /**
     * Close database connection
     */
    public void disconnect() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                logger.info("Database connection closed successfully");
            }
        } catch (Exception e) {
            logger.error("Error closing database connection", e);
        }
    }

    /**
     * Check if connection is active
     * @return true if connected, false otherwise
     */
    public boolean isConnected() {
        try {
            return connection != null && !connection.isClosed();
        } catch (Exception e) {
            return false;
        }
    }
}
