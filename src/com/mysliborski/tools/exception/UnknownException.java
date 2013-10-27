package com.mysliborski.tools.exception;


public class UnknownException extends ServiceException {

	private static final long serialVersionUID = -711496532571618056L;

	public UnknownException(Exception e) {
		super(e, ServiceExceptionCodes.UNKNOWN);
	}
	
}
