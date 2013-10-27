package com.mysliborski.tools.async;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Build;

import com.mysliborski.tools.exception.ServiceException;
import com.mysliborski.tools.exception.UnknownException;
import com.mysliborski.tools.helper.GeneralHelper;

import static com.mysliborski.tools.helper.LogHelper.log;

public abstract class AMAsyncTask<R> extends AsyncTask<Void, Void, R> {

	private final DataCallback<R> callback;
	private ServiceException exception;

	public AMAsyncTask(DataCallback<R> callback) {
		this.callback = callback;
	}

	@Override
	protected R doInBackground(Void... params) {
		try {
			return backgroundWork();
		} catch (ServiceException e) {
			this.exception = e;
			return null;
		} catch (Exception e) {
			log(e, "Background work");
			this.exception = new UnknownException(e);
			return null;
		}
	}

	protected abstract R backgroundWork() throws Exception;

	protected void onPostExecute(R result) {
		if (callback != null) {
			if (exception == null) {
				callback.onLoaded(result);
			} else {
				log(this.exception, "AMAsyncTask - Exception");
				callback.onFailure(this.exception);
			}
		}
	};

	/**
	 * Since 3.0 (v11) execute runs all AsyncTasks in single thread
	 */
	@SuppressLint("NewApi")
	public void executeAsParallel() {

		if (Build.VERSION.SDK_INT < 11) {
			execute();
		} else {
			executeOnExecutor(THREAD_POOL_EXECUTOR);
		}
	}
}
