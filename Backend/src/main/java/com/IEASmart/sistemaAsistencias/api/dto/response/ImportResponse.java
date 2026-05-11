package com.IEASmart.sistemaAsistencias.api.dto.response;

public class ImportResponse {
    private String status;
    private String message;

    public ImportResponse(String status, String message) {
        this.status = status;
        this.message = message;
    }

    public ImportResponse() {}

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
