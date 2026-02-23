package com.IEASmart.sistemaAsistencias.api.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.time.LocalDateTime;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ErrorResponse {
    private LocalDateTime timestamp;
    private int status;
    private String error;
    private String message;
    private String code;
    private String path;
    private Map<String, String> validationErrors;

    public ErrorResponse() {
    }

    public static Builder builder() {
        return new Builder();
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
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

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public Map<String, String> getValidationErrors() {
        return validationErrors;
    }

    public void setValidationErrors(Map<String, String> validationErrors) {
        this.validationErrors = validationErrors;
    }

    public static class Builder {
        private final ErrorResponse instance = new ErrorResponse();

        public Builder timestamp(LocalDateTime timestamp) {
            instance.setTimestamp(timestamp);
            return this;
        }

        public Builder status(int status) {
            instance.setStatus(status);
            return this;
        }

        public Builder error(String error) {
            instance.setError(error);
            return this;
        }

        public Builder message(String message) {
            instance.setMessage(message);
            return this;
        }

        public Builder code(String code) {
            instance.setCode(code);
            return this;
        }

        public Builder path(String path) {
            instance.setPath(path);
            return this;
        }

        public Builder validationErrors(Map<String, String> validationErrors) {
            instance.setValidationErrors(validationErrors);
            return this;
        }

        public ErrorResponse build() {
            return instance;
        }
    }
}
