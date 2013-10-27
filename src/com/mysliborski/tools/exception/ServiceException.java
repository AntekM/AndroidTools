package com.mysliborski.tools.exception;

public class ServiceException extends Exception {

    final private int exceptionCode;

    public ServiceException(int exceptionCode) {
        this.exceptionCode = exceptionCode;
    }

    public ServiceException(int exceptionCode, String message) {
        super(message);
        this.exceptionCode = exceptionCode;
    }

    public ServiceException(Exception wrappedException, int exceptionCode) {
        super(wrappedException);
        this.exceptionCode = exceptionCode;
    }

    public int getExceptionCode() {
        return exceptionCode;
    }
}