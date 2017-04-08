package com.oklab.umbrella.asynctasks;

import android.content.Context;
import android.util.Log;

import com.oklab.umbrella.R;
import com.oklab.umbrella.data.HTTPConnectionResult;

import org.apache.commons.io.IOUtils;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

/**
 * Created by olgakuklina on 2017-04-08.
 */

public class HTTPConnectionService {
    private static final String TAG = HTTPConnectionService.class.getSimpleName();
    private final String uri;
    private final Context context;

    public HTTPConnectionService(String uri, Context context) {
        this.uri = uri;
        this.context = context;
    }

    public HTTPConnectionResult establishConnection() {
        String Uri = context.getString(R.string.base_uri, uri);
        try {

            HttpURLConnection connect = (HttpURLConnection) new URL(Uri).openConnection();
            connect.connect();
            int responseCode = connect.getResponseCode();

            Log.v(TAG, "responseCode = " + responseCode);
            InputStream inputStream = connect.getInputStream();
            String response = IOUtils.toString(inputStream, StandardCharsets.UTF_8);
            Log.v(TAG, "response = " + response);
            return new HTTPConnectionResult(response, responseCode);

        } catch (Exception e) {
            Log.e(TAG, "Get data failed", e);
            return null;
        }
    }
}


