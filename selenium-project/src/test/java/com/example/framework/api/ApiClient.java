package com.example.framework.api;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * REST API client wrapper for API testing
 * Provides methods for GET, POST, PUT, DELETE, PATCH requests
 */
public class ApiClient {
    private static final Logger logger = LogManager.getLogger(ApiClient.class);
    private RequestSpecification requestSpecification;
    private String baseUrl;

    public ApiClient(String baseUrl) {
        this.baseUrl = baseUrl;
        this.requestSpecification = RestAssured.given().baseUri(baseUrl);
    }

    /**
     * Set base URI for all requests
     */
    public ApiClient baseUri(String baseUri) {
        this.requestSpecification = this.requestSpecification.baseUri(baseUri);
        return this;
    }

    /**
     * Add header to request
     */
    public ApiClient header(String key, String value) {
        this.requestSpecification = this.requestSpecification.header(key, value);
        logger.debug("Added header: {} = {}", key, value);
        return this;
    }

    /**
     * Add query parameter
     */
    public ApiClient queryParam(String key, String value) {
        this.requestSpecification = this.requestSpecification.queryParam(key, value);
        logger.debug("Added query param: {} = {}", key, value);
        return this;
    }

    /**
     * Add path parameter
     */
    public ApiClient pathParam(String key, String value) {
        this.requestSpecification = this.requestSpecification.pathParam(key, value);
        logger.debug("Added path param: {} = {}", key, value);
        return this;
    }

    /**
     * Set request body
     */
    public ApiClient body(Object body) {
        this.requestSpecification = this.requestSpecification.body(body);
        logger.debug("Set request body: {}", body);
        return this;
    }

    /**
     * Set content type
     */
    public ApiClient contentType(String contentType) {
        this.requestSpecification = this.requestSpecification.contentType(contentType);
        return this;
    }

    /**
     * Perform GET request
     */
    public Response get(String endpoint) {
        logger.info("GET request to: {}{}", baseUrl, endpoint);
        Response response = this.requestSpecification.when().get(endpoint);
        logResponse(response);
        return response;
    }

    /**
     * Perform POST request
     */
    public Response post(String endpoint) {
        logger.info("POST request to: {}{}", baseUrl, endpoint);
        Response response = this.requestSpecification.when().post(endpoint);
        logResponse(response);
        return response;
    }

    /**
     * Perform PUT request
     */
    public Response put(String endpoint) {
        logger.info("PUT request to: {}{}", baseUrl, endpoint);
        Response response = this.requestSpecification.when().put(endpoint);
        logResponse(response);
        return response;
    }

    /**
     * Perform DELETE request
     */
    public Response delete(String endpoint) {
        logger.info("DELETE request to: {}{}", baseUrl, endpoint);
        Response response = this.requestSpecification.when().delete(endpoint);
        logResponse(response);
        return response;
    }

    /**
     * Perform PATCH request
     */
    public Response patch(String endpoint) {
        logger.info("PATCH request to: {}{}", baseUrl, endpoint);
        Response response = this.requestSpecification.when().patch(endpoint);
        logResponse(response);
        return response;
    }

    /**
     * Perform HEAD request
     */
    public Response head(String endpoint) {
        logger.info("HEAD request to: {}{}", baseUrl, endpoint);
        Response response = this.requestSpecification.when().head(endpoint);
        logResponse(response);
        return response;
    }

    /**
     * Log response details
     */
    private void logResponse(Response response) {
        logger.info("Response Status Code: {}", response.getStatusCode());
        logger.debug("Response Time: {}ms", response.getTime());
        logger.debug("Response Content-Type: {}", response.getContentType());
    }

    /**
     * Reset request specification for next request
     */
    public ApiClient reset() {
        this.requestSpecification = RestAssured.given().baseUri(baseUrl);
        return this;
    }
}
