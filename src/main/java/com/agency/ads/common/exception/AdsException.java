package com.agency.ads.common.exception;

public class AdsException extends RuntimeException{

    private Integer status;
    private String errorCode;

    public AdsException() {

    }

    public AdsException(int status, String errorCode, String message) {
        super(message);
        this.status = status;
        this.errorCode = errorCode;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }
}
