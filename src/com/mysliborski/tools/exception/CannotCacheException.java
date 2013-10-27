package com.mysliborski.tools.exception;

public class CannotCacheException extends ServiceException {

	private static final long serialVersionUID = -1849999783430481351L;

	public CannotCacheException() {
		super(ServiceExceptionCodes.CANNOT_CACHE);
	}

}
