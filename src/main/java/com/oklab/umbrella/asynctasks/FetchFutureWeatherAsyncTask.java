package com.oklab.umbrella.asynctasks;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.oklab.umbrella.R;
import com.oklab.umbrella.data.WeatherDataEntry;
import com.oklab.umbrella.parsers.WeatherDataParser;

import org.apache.commons.io.IOUtils;
import org.json.JSONObject;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

/**
 * Created by olgakuklina on 2017-04-08.
 */

public class FetchFutureWeatherAsyncTask extends AsyncTask<Integer, Void, WeatherDataEntry> {
    private static final String TAG = FetchFutureWeatherAsyncTask.class.getSimpleName();
    private final Context context;
    private OnWeatherDataLoadedListener listener;
    private int page;

    public FetchFutureWeatherAsyncTask(Context context, OnWeatherDataLoadedListener listener) {
        this.context = context;
        this.listener = listener;
    }

    @Override
    protected WeatherDataEntry doInBackground(Integer... args) {
        page = args[0];

        String future = context.getString(R.string.future, page);
        String uri = context.getString(R.string.base_uri, future);
        try {
            HttpURLConnection connect = (HttpURLConnection) new URL(uri).openConnection();
            connect.connect();
            int responseCode = connect.getResponseCode();

            Log.v(TAG, "responseCode = " + responseCode);
            InputStream inputStream = connect.getInputStream();
            String response = IOUtils.toString(inputStream, StandardCharsets.UTF_8);
            Log.v(TAG, "response = " + response);
            JSONObject jsonObject = new JSONObject(response);

            return new WeatherDataParser().parse(jsonObject);

        } catch (Exception e) {
            Log.e(TAG, "Get data failed", e);
            return null;
        }
    }

    @Override
    protected void onPostExecute(WeatherDataEntry entryData) {
        super.onPostExecute(entryData);
        listener.OnWeatherDataLoaded(entryData, page);
    }

    public interface OnWeatherDataLoadedListener {
        void OnWeatherDataLoaded(WeatherDataEntry weatherDataEntry, int page);
    }
}
