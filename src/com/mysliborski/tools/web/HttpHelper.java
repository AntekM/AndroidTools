package com.mysliborski.tools.web;

import static com.mysliborski.tools.helper.GeneralHelper.concat;
import static com.mysliborski.tools.helper.LogHelper.log;
import static com.mysliborski.tools.helper.LogHelper.loge;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import android.net.ConnectivityManager;

import com.mysliborski.tools.exception.UnauthorizedAccessException;
import com.mysliborski.tools.exception.WebServiceException;
import com.mysliborski.tools.helper.IOHelper;
import com.mysliborski.tools.helper.LogHelper;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;

import android.util.Log;

import com.mysliborski.tools.Constants;
import com.mysliborski.tools.exception.ConnectionProblemException;
import com.mysliborski.tools.exception.ServiceException;
import com.mysliborski.tools.exception.TaskCancelledException;

public class HttpHelper {

    private ConnectivityManager connectivityManager;

    public static HttpHelper getInstance(ConnectivityManager connectivityManager) {
        if (instance==null) {
            instance = new HttpHelper(connectivityManager);
        }
        return instance;
    }

    private static HttpHelper instance;

	private HttpHelper(ConnectivityManager connectivityManager) {
        this.connectivityManager = connectivityManager;
    }

	/**
	 * Initializes download of a file from a given URL
	 * 
	 * @return
	 * @throws ServiceException
	 */
	public HttpEntity initDownload(final HttpUriRequest request) throws ServiceException {
        if (connectivityManager.getActiveNetworkInfo()!=null) {
            if (!connectivityManager.getActiveNetworkInfo().isConnectedOrConnecting()) {
                throw new ConnectionProblemException();
            }
        } else {
            throw new ConnectionProblemException();
        }
		try {
			BasicHttpParams httpParameters = new BasicHttpParams();
			HttpConnectionParams.setConnectionTimeout(httpParameters, Constants.CONNECTION_TIMEOUT);
			HttpConnectionParams.setSoTimeout(httpParameters, Constants.SOCKET_TIMEOUT);
			DefaultHttpClient client = new DefaultHttpClient(httpParameters);
            HttpResponse response = client.execute(request);
			StatusLine statusLine = response.getStatusLine();
			int statusCode = statusLine.getStatusCode();
			if (statusCode == 200) {
				return response.getEntity();
			} else if (statusCode == 401) {
                throw new UnauthorizedAccessException();
            }
            else {
                // TODO consume content and log
                HttpEntity entity = response.getEntity();
                String reply = new String(IOHelper.getInputStreamContent(entity.getContent()));
                entity.consumeContent();
                loge(null, concat("HTTPHelper : getAsInputStream. StatusCode: ", statusCode, ", reply: ", reply));
                throw new WebServiceException(statusCode);
			}
        } catch (ServiceException e) {
            throw e;
		} catch (Exception e) {
			log(e, "HTTPHelper : getAsInputStream. Error downloading file");
			throw new ConnectionProblemException(e);
		}
	}

	/**
	 * Does a download process of remote file and notifies progressObserver on a
	 * way. Does nothing more
	 * 
	 * @param entity
	 * @param os
	 * @param progressCallback
	 * @throws TaskCancelledException
	 * @throws IOException
	 */
	public void doDownloadWithProgress(final HttpEntity entity, OutputStream os,
			ProgressObserver<Integer> progressCallback) throws TaskCancelledException, IOException {
		int sumDownloaded = 0;
		try {
			InputStream is = null;
			byte[] buffer = new byte[Constants.BUFFER_SIZE];
			int bytesRead;
			log("Starting download");
			is = new BufferedInputStream(entity.getContent(), Constants.BUFFER_SIZE);
			while ((bytesRead = is.read(buffer)) > -1) {
				os.write(buffer, 0, bytesRead);
				sumDownloaded += bytesRead;
				if (progressCallback != null) {
					progressCallback.updateProgress(bytesRead);
					if (progressCallback.isCancelled()) {
						throw new TaskCancelledException();
					}
				}
			}
			log("Finished downloading without break");
			os.flush();
		} finally {
			log("Downloaded ", sumDownloaded, " bytes");
			entity.consumeContent();
		}
	}

}
