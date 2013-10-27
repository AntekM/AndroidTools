package com.mysliborski.tools.cache;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;

import android.content.Context;
import android.net.ConnectivityManager;
import com.mysliborski.tools.helper.LogHelper;

import org.apache.http.HttpEntity;

import android.os.Environment;
import android.util.Log;

import com.mysliborski.tools.Constants;
import com.mysliborski.tools.exception.CannotCacheException;
import com.mysliborski.tools.exception.ConnectionProblemException;
import com.mysliborski.tools.exception.ServiceException;
import com.mysliborski.tools.exception.TaskCancelledException;
import com.mysliborski.tools.helper.GeneralHelper;
import com.mysliborski.tools.web.HttpHelper;
import com.mysliborski.tools.web.ProgressObserver;

import static com.mysliborski.tools.helper.LogHelper.log;

public class FileCacheHelper {

	public interface CacheSizeLimit {
		int getCacheSizeLimit();
	}

    public FileCacheHelper(Context context) {
        this.httpHelper = HttpHelper.getInstance((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE));
    }

    HttpHelper httpHelper;

	public static boolean canWriteExternal() {
		return Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState());
	}

	/**
	 * Downloads contents of open HttpEntity to cache and returns File handler
	 * to created resource.
	 * 
	 * @param entity
	 * @param progressCallback
	 * @return
	 * @throws CannotCacheException
	 * @throws TaskCancelledException
	 */
	public void downloadToCache(HttpEntity entity, File cacheFile, ProgressObserver<Integer> progressCallback)
			throws CannotCacheException, TaskCancelledException {
        try {
		OutputStream fos = new BufferedOutputStream(new FileOutputStream(cacheFile), Constants.BUFFER_SIZE);
		boolean finishedCorrectly = false;
		try {
			httpHelper.doDownloadWithProgress(entity, fos, progressCallback);
			finishedCorrectly = true;
			Log.i("FileCache", "Zakonczony download dla " + cacheFile.getName());
		} catch (TaskCancelledException e) {
			Log.i("FileCache : cacheDownload", "Cancelled!");
			throw e;
		} catch (Exception e) {
			Log.e("FileCache : cacheDownload", "Exception", e);
			throw new CannotCacheException();
		} finally {
			Log.i("FileCache", "Closing file");
			fos.close();
			if (!finishedCorrectly) {
				Log.i("FileCache : cacheDownload", "Attempt to delete cache file");
				cacheFile.delete();
			}
		}
        } catch (IOException e) {
            throw new CannotCacheException();
        }

	}

	
	public File downloadToCache(String urlS, File cacheFile) throws ServiceException {
		try {
			OutputStream fos = null;
			InputStream input = null;
			int total = 0;
			try {
				URL url = new URL(urlS);
				URLConnection connection = url.openConnection();
				connection.connect();
				fos = new BufferedOutputStream(new FileOutputStream(cacheFile), Constants.BUFFER_SIZE);
				input = new BufferedInputStream(url.openStream());
				byte data[] = new byte[1024];
				int count;
				while ((count = input.read(data)) != -1) {
					total += count;
					fos.write(data, 0, count);
				}
				fos.flush();
				fos.close();
				input.close();
			} finally {
				log("Downloaded ", total);
				fos.flush();
				fos.close();
				input.close();
			}
			// val entity = httpHelper.initDownload(new HttpGet(url));
			// fileCacheHelper.downloadToCache(entity, cacheFile, null);
			return cacheFile;
		} catch (Exception e) {
			log("Nie udalo sie");
			Log.i("FileCache : cacheDownload", "Attempt to delete cache file");
			cacheFile.delete();
			throw new ConnectionProblemException(e);
		}
	}
}
