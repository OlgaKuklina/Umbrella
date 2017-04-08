package com.oklab.umbrella.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.oklab.umbrella.R;
import com.oklab.umbrella.Utils.Utils;
import com.oklab.umbrella.asynctasks.FetchCurrentWeatherLoader;
import com.oklab.umbrella.asynctasks.FetchFutureWeatherAsyncTask;
import com.oklab.umbrella.data.WeatherDataEntry;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements FetchFutureWeatherAsyncTask.OnCWeatherDataLoadedListener {
    private static final String TAG = MainActivity.class.getSimpleName();
    private static final String AVG = "avg";
    private static final int NUMBER_OF_FUTURE_DAYS = 5;
    public WeatherDataEntry weatherdataEntry;
    List<Double> entryList;
    private TextView windSpeed;
    private TextView tempC;
    private TextView tempF;
    private TextView temp;
    private TextView location;
    private TextView avgC;
    private TextView avg;
    private TextView avgF;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Log.v(TAG, "onCreate");
        Bundle bundle = new Bundle();
        bundle.putInt("task", 1);
        //The loader will fetch data only once and will respond with the same data
        // upon orientation change without connecting to network.
        getSupportLoaderManager().initLoader(0, bundle, new MainActivity.WeatherLoaderCallbacks());
        windSpeed = (TextView) findViewById(R.id.wind_speed);
        tempC = (TextView) findViewById(R.id.temp_c);
        tempF = (TextView) findViewById(R.id.temp_f);
        temp = (TextView) findViewById(R.id.temp);
        location = (TextView) findViewById(R.id.title);
        avg = (TextView) findViewById(R.id.avg);
        avgC = (TextView) findViewById(R.id.avg_c);
        avgF = (TextView) findViewById(R.id.avg_f);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                entryList = new ArrayList<>(NUMBER_OF_FUTURE_DAYS);
                for (int i = 1; i < 6; i++) {
                    FetchFutureWeatherAsyncTask task = new FetchFutureWeatherAsyncTask(MainActivity.this, MainActivity.this);
                    task.execute(i);
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent intent = new Intent(this, SettingsActivity.class);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void populateUIData(WeatherDataEntry dataEntry) {
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
        String tempSettings = sharedPref.getString("temp_list", "1");
        Log.v(TAG, "tempSettings" + tempSettings);
        Log.v(TAG, "tempSettings = " + tempSettings);
        if (tempSettings.equals("0")) {
            tempC.setVisibility(View.INVISIBLE);
            tempF.setVisibility(View.VISIBLE);
            avgC.setVisibility(View.INVISIBLE);
            avgF.setVisibility(View.VISIBLE);
            temp.setText(Double.toString(Utils.celsiusToFahrenheit(dataEntry.getTemp())));
        } else if (tempSettings.equals("1")) {
            tempC.setVisibility(View.VISIBLE);
            tempF.setVisibility(View.INVISIBLE);
            avgC.setVisibility(View.VISIBLE);
            avgF.setVisibility(View.INVISIBLE);
            temp.setText(Double.toString(dataEntry.getTemp()));
        }
        windSpeed.setText(Double.toString(dataEntry.getSpeed()));
        location.setText(dataEntry.getName());
    }

    @Override
    public void OnCWeatherDataLoaded(WeatherDataEntry entry) {
        Log.v(TAG, "entry" + entry.getTemp());

        entryList.add(entry.getTemp());
        Log.v(TAG, " entry size = " + entryList.size());
        if (entryList.size() == 5) {
            populateAVG(entryList);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.v(TAG, "onSaveInstanceState ");
        if(avg.getText()!=null) {
            outState.putString(AVG, avg.getText().toString());
        }
    }
    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        Log.v(TAG, "onRestoreInstanceState " + savedInstanceState);
        avg.setText(savedInstanceState.getString(AVG));
    }

    public void populateAVG(List<Double> dataEntryList) {
        Log.v(TAG, "dataEntryList = " + dataEntryList);
        double stdDev = Utils.stddev(dataEntryList);
        Log.v(TAG, "avgData = " + stdDev);
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
        String tempSettings = sharedPref.getString("temp_list", "1");
        Log.v(TAG, "tempSettings" + tempSettings);
        Log.v(TAG, "tempSettings = " + tempSettings);
        if (tempSettings.equals("0")) {
            avgC.setVisibility(View.INVISIBLE);
            avgF.setVisibility(View.VISIBLE);
            avg.setText(String.format(Locale.getDefault(), "%.2f", Utils.celsiusToFahrenheit(stdDev)));
        } else if (tempSettings.equals("1")) {
            avgC.setVisibility(View.VISIBLE);
            avgF.setVisibility(View.INVISIBLE);
            avg.setText(String.format(Locale.getDefault(), "%.2f", stdDev));
        }
    }

    private class WeatherLoaderCallbacks implements LoaderManager.LoaderCallbacks<WeatherDataEntry> {

        @Override
        public Loader<WeatherDataEntry> onCreateLoader(int id, Bundle args) {
            return new FetchCurrentWeatherLoader(getApplicationContext());
        }

        @Override
        public void onLoadFinished(Loader<WeatherDataEntry> loader, WeatherDataEntry dataEntry) {
            if (dataEntry != null) {
                weatherdataEntry = dataEntry;
                Log.v(TAG, "dataEntry" + dataEntry.getName());
                getLoaderManager().destroyLoader(loader.getId());
                populateUIData(weatherdataEntry);
                return;
            }
            getLoaderManager().destroyLoader(loader.getId());
        }

        @Override
        public void onLoaderReset(Loader<WeatherDataEntry> loader) {
        }
    }
}
