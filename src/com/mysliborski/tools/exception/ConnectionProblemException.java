package com.mysliborski.tools.exception;

public class ConnectionProblemException extends ServiceException {

	private static final long serialVersionUID = 1L;

	public ConnectionProblemException(Exception cause) {
		super(cause, ServiceExceptionCodes.CONNECTION_PROBLEM);
	}

	public ConnectionProblemException() {
		super(ServiceExceptionCodes.CONNECTION_PROBLEM);
	}

    public ConnectionProblemException(int httpErrorCode) {
        super(ServiceExceptionCodes.CONNECTION_PROBLEM, "HTTP error code: "+httpErrorCode);
    }
}
