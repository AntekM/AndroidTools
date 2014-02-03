package com.mysliborski.tools.exception;

/**
 * Created by antonimysliborski on 27/10/2013.
 */
public class WebServiceException extends ConnectionProblemException {

    private int httpErrorCode;

    public WebServiceException(Exception cause) {
        super(cause);
    }

    public WebServiceException() {
        super();
    }

    public WebServiceException(int httpErrorCode) {
        super(httpErrorCode);
        this.httpErrorCode = httpErrorCode;
    }

    public int getHttpErrorCode() {
        return httpErrorCode;
    }
}
