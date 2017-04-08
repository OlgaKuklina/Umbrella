package com.oklab.umbrella.asynctasks;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;
import android.util.Log;

import com.oklab.umbrella.R;
import com.oklab.umbrella.data.HTTPConnectionResult;
import com.oklab.umbrella.data.WeatherDataEntry;
import com.oklab.umbrella.parsers.WeatherDataParser;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by olgakuklina on 2017-04-08.
 */

public class FetchCurrentWeatherLoader extends AsyncTaskLoader<WeatherDataEntry> {
    private static final String TAG = FetchCurrentWeatherLoader.class.getSimpleName();

    public FetchCurrentWeatherLoader(Context context) {
        super(context);
    }

    @Override
    protected void onStartLoading() {
        super.onStartLoading();
        forceLoad();
    }

    @Override
    public WeatherDataEntry loadInBackground() {
        String uri = getContext().getString(R.string.uri);
        HTTPConnectionService fetchHTTPConnectionService = new HTTPConnectionService(uri, this.getContext());
        HTTPConnectionResult result = fetchHTTPConnectionService.establishConnection();
        Log.v(TAG, "responseCode = " + result.getResponceCode());
        Log.v(TAG, "result = " + result.getResult());
        try {
            JSONObject jsonObject = new JSONObject(result.getResult());
            return new WeatherDataParser().parse(jsonObject);

        } catch (JSONException e) {
            Log.e(TAG, "", e);
        }
        return null;
    }
}


