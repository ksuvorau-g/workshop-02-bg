package com.workshop.exchangerates.dto;

public class FetchResponse {
    private String message;
    private String currency;
    private String status;

    public FetchResponse() {
    }

    public FetchResponse(String message, String currency, String status) {
        this.message = message;
        this.currency = currency;
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
