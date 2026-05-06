package com.internship.tool.dto;

import java.time.LocalDateTime;
import java.util.Map;

public class ErrorResponse {

    private String timestamp;
    private int status;
    private String error;
    private String message;
    private Map<String, String> errors; // for validation errors

    public ErrorResponse() {
        this.timestamp = LocalDateTime.now().toString();
    }

    public ErrorResponse(int status, String error, String message) {
        this.timestamp = LocalDateTime.now().toString();
        this.status = status;
        this.error = error;
        this.message = message;
    }

    public ErrorResponse(int status, String error, String message, Map<String, String> errors) {
        this.timestamp = LocalDateTime.now().toString();
        this.status = status;
        this.error = error;
        this.message = message;
        this.errors = errors;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Map<String, String> getErrors() {
        return errors;
    }

    public void setErrors(Map<String, String> errors) {
        this.errors = errors;
    }
}
