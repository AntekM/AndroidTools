package com.mysliborski.tools.cache;

import java.io.File;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.os.Environment;

public class CacheCompatibility {

	/**
	 * Returns the absolute path to the directory on the external filesystem
	 * (that is somewhere on Environment.getExternalStorageDirectory()) where
	 * the application can place persistent files it owns.
	 * 
	 * @param context
	 * @return Returns the path of the directory holding application files on
	 *         external storage. Returns null if external storage is not
	 *         currently mounted so it could not ensure the path exists; you
	 *         will need to call this method again when it is available.
	 */
	@SuppressLint("NewApi")
	public static File getExternalFilesDir(Context context) {
		File dir;
		if (Build.VERSION.SDK_INT < Build.VERSION_CODES.FROYO) {
			dir = new File(Environment.getExternalStorageDirectory().getPath() + "/Android/data/"
					+ context.getPackageName() + "/files/");
			if (!dir.exists() && !dir.mkdirs())
				dir = null;
		} else {
			dir = context.getExternalFilesDir(null);
		}
		return dir;
	}

	/**
	 * Returns the absolute path to the directory on the external filesystem
	 * (that is somewhere on Environment.getExternalStorageDirectory() where the
	 * application can place cache files it owns.
	 * 
	 * @param context
	 * @return Returns the path of the directory holding application cache files
	 *         on external storage. Returns null if external storage is not
	 *         currently mounted so it could not ensure the path exists; you
	 *         will need to call this method again when it is available.
	 */
	@SuppressLint("NewApi")
	public static File getExternalCacheDir(Context context) {
		File dir;
		if (Build.VERSION.SDK_INT < Build.VERSION_CODES.FROYO) {
			dir = new File(Environment.getExternalStorageDirectory().getPath() + "/Android/data/"
					+ context.getPackageName() + "/cache/");
			if (!dir.exists() && !dir.mkdirs())
				dir = null;
		} else {
			dir = context.getExternalCacheDir();
		}
		return dir;
	}

}
