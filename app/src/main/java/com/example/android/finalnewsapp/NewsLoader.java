package com.example.android.finalnewsapp;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.util.List;

public class NewsLoader extends AsyncTaskLoader<List<News>> {

    private static final String LOG_TAG = NewsLoader.class.getName();

    private String nUrl;

    public NewsLoader (Context context, String url){
        super(context);
        nUrl = url;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    public List<News> loadInBackground(){
        if (nUrl == null){
            return null;
        }

        List<News> news = HttpHandler.fetchNewsData(nUrl);
        return news;
    }
}