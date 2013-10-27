package com.mysliborski.tools.exception;

/**
 * Created by antonimysliborski on 02/10/2013.
 */
public class UnauthorizedAccessException extends ServiceException {

    public UnauthorizedAccessException() {
        super(ServiceExceptionCodes.UNAUTHORIZED_ACCESS);
    }
}
