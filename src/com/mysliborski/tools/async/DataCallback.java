package com.mysliborski.tools.async;

import com.mysliborski.tools.exception.ServiceException;

public interface DataCallback<T> {

	void onLoaded(T data);
	
	void onFailure(ServiceException e);
	
}
