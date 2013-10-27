package com.mysliborski.tools.web;

public interface ProgressObserver<T> {

	/**
	 * Method is called when the long task is started (eg. file needs to be
	 * downloaded from Internet, is not present cache). Receiver should show
	 * progress bar.
	 */
	void startLongTask();

	/**
	 * Method is called when progress shoud be updated.
	 * 
	 * @param values
	 */
	void updateProgress(T progress);

	void registerCancellable(Cancellable cancellable);

	boolean isCancelled();
}
