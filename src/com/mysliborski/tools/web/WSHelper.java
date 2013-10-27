package com.mysliborski.tools.web;

import android.content.Context;
import android.net.ConnectivityManager;
import com.mysliborski.tools.exception.ConnectionProblemException;
import com.mysliborski.tools.exception.ServiceException;
import com.mysliborski.tools.exception.ServiceExceptionCodes;
import com.mysliborski.tools.helper.IOHelper;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

import static com.mysliborski.tools.helper.LogHelper.log;

public class WSHelper {

    HttpHelper httpHelper;

    public WSHelper(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        httpHelper = HttpHelper.getInstance(connectivityManager);
    }

    public String doGetRequest(final String url) throws ServiceException {
        log(" doGet: " + url);
        HttpGet getRequest = new HttpGet(url);
        HttpEntity response = httpHelper.initDownload(getRequest);
        try {
            try {
                return streamToString(response.getContent());
            } finally {
                if (response != null)
                    response.consumeContent();
            }
        } catch (IOException e) {
            throw new ConnectionProblemException(e);
        }
    }

    public String postRequest(final String url, final String body) throws ServiceException {
        try {
            HttpPost postRequest = new HttpPost(url);
            StringEntity entity = new StringEntity(body, "utf-8");
            postRequest.setEntity(entity);
            postRequest.setHeader("Accept", "application/json");
            postRequest.setHeader("Content-Type", "application/json;");
            HttpEntity response = httpHelper.initDownload(postRequest);
            try {
                return streamToString(response.getContent());
            } finally {
                if (response != null)
                    response.consumeContent();
            }
        } catch (ServiceException e) {
            throw e;
        } catch (Exception e) {
            log(e, "PostRequest " + url);
            throw new ConnectionProblemException();
        }
    }

    /**
     * Reads contents of stream to String and closes it afterwards
     *
     * @param stream
     * @return
     */
    private String streamToString(InputStream stream) throws ServiceException {
        return new String(IOHelper.getInputStreamContent(stream));
    }
}
