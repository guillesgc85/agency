package com.agency.ads.common.response;

public class ErrorResponse {

    private  ExceptionErrorResponse error;

    public ErrorResponse() {
    }

    public ErrorResponse(ExceptionErrorResponse error) {
        this.error = error;
    }

    public ExceptionErrorResponse getError() {
        return error;
    }

    public void setError(ExceptionErrorResponse error) {
        this.error = error;
    }
}
