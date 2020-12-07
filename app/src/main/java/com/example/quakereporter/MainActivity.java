package com.example.quakereporter;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {



    public static final String LOG_TAG = MainActivity.class.getName();
    public static final String sUrl = "https://earthquake.usgs.gov/fdsnws/event/1/query?";

    private TextView mEmptyTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();


        if(networkInfo != null && networkInfo.isConnected()) {
            QuakeAsyncTask task = new QuakeAsyncTask();
            task.execute(sUrl);
        }else {
            ProgressBar progressBar = (ProgressBar) findViewById(R.id.progress_bar);
            progressBar.setVisibility(View.GONE);
            mEmptyTextView = (TextView) findViewById(R.id.empty_text_view);
            mEmptyTextView.setText("You're offline");
        }

    }

//    @NonNull
//    @Override
//    public Loader<String> onCreateLoader(int id, @Nullable Bundle args) {
//        return new QuakeLoader(this,sUrl);
//    }
//
//    @Override
//    public void onLoadFinished(@NonNull Loader<String> loader, String data) {
//        updateUi(data);
//    }
//
//    @Override
//    public void onLoaderReset(@NonNull Loader<String> loader) {
//
//    }


    @Override
    protected void onStart() {
        super.onStart();
    }

    private void updateUi(String jsonObject){

        ArrayList<QuakeDetails> earthquakes = QueryUtils.extractEarthquakes(jsonObject);
        ListView earthquakeListView = (ListView) findViewById(R.id.list);

        final DetailsAdapter detailsAdapter = new DetailsAdapter(this, earthquakes);
        earthquakeListView.setAdapter(detailsAdapter);

        mEmptyTextView = (TextView) findViewById(R.id.empty_text_view);
        earthquakeListView.setEmptyView(mEmptyTextView);

        earthquakeListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                QuakeDetails quakeDetails = detailsAdapter.getItem(position);
                Uri uri = Uri.parse(quakeDetails.grtUrl());
                Intent webIntent = new Intent(Intent.ACTION_VIEW,uri);
                startActivity(webIntent);
            }
        });
    }

    public class QuakeAsyncTask extends AsyncTask<String,Void,String> {


        @Override
        protected String doInBackground(String... urls) {

            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
            String minMag = sharedPreferences.getString(getString(R.string.settings_min_magnitude_key),getString(R.string.settings_min_magnitude_default));
            String order = sharedPreferences.getString(getString(R.string.settings_order_by_key),getString(R.string.settings_order_by_default));
            Uri baseUri = Uri.parse(urls[0]);
            Uri.Builder uriBuilder = baseUri.buildUpon();
            uriBuilder.appendQueryParameter("format","geojson");
            uriBuilder.appendQueryParameter("minmag",minMag);
            uriBuilder.appendQueryParameter("orderby",order);
            URL mUrl = QueryUtils.createURL(uriBuilder.toString());


            String jsonObject = null;
            try {
                jsonObject = QueryUtils.makeHttpRequest(mUrl);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return  jsonObject;
        }

        @Override
        protected void onPostExecute(String jsonObject) {
            ProgressBar progressBar = (ProgressBar) findViewById(R.id.progress_bar);
            progressBar.setVisibility(View.GONE);
            updateUi(jsonObject);
            mEmptyTextView.setText("No earthquakes");
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.settings){
            Intent settingsIntent = new Intent(this,SettingsActivity.class);
            startActivity(settingsIntent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}