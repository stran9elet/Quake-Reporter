package com.example.quakereporter;

import android.content.AsyncTaskLoader;
import android.content.Context;

import androidx.annotation.Nullable;

import java.io.IOException;
import java.net.URL;

public class QuakeLoader extends AsyncTaskLoader<String> {

    private URL mUrl;

    public QuakeLoader(Context context, String sUrl){
        super(context);
        mUrl = QueryUtils.createURL(sUrl);
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    @Nullable
    @Override
    public String loadInBackground() {
        String jsonObject = "";
        try {
            jsonObject = QueryUtils.makeHttpRequest(mUrl);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }
}
