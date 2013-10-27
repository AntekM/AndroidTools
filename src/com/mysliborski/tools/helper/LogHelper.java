package com.mysliborski.tools.helper;

import android.util.Log;

public class LogHelper {

	public static final String TAG_APP = "application";

	public static void loge(final Exception e, final String message) {
		Log.e(TAG_APP, message, e);
	}
	
	public static void loge(final String message) {
		Log.e(TAG_APP, message);
	}
	
	public static void log(final String message) {
		Log.i(TAG_APP, message);
	}

	public static void log(final Object... message) {
		String messageStr = GeneralHelper.concat(message);
		Log.i(TAG_APP, messageStr);
	}

	public static void logv(final String message) {
		Log.v(TAG_APP, message);
	}

	public static void logTag(final String tag, final Object... message) {
		String messageStr = GeneralHelper.concat(message);
		Log.i(TAG_APP+tag, messageStr);
	}
	
	public static void logObj(final Object obj, final String message) {
		final Class<?> objClass = obj.getClass();
		String _plus = (objClass + ": ");
		String _plus_1 = (_plus + message);
		Log.i(TAG_APP, _plus_1);
	}

}
