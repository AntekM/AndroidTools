package com.mysliborski.tools.web;

public interface Cancellable {
	
	void cancelTask(); 
	
	boolean isCancelled();
}
