package com.example.android.finalnewsapp;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;


import android.app.LoaderManager.LoaderCallbacks;
import java.util.ArrayList;
import java.util.List;

public class NewsActivity extends AppCompatActivity
             implements LoaderCallbacks<List<News>>{
   // SharedPreferences.OnSharedPreferenceChangeListener {


    //**URL for news articles*/
    private static final String NEWS_QUERY_URL = "https://content.guardianapis.com/search?api-key=36a4f125-fc53-4098-be3d-f259802af783";

    //**Loader ID*/
    private static final int NEWS_LOADER_ID = 1;

    /**Adapter for list of articles*/
    private NewsAdapter nAdapter;

    //**Empty TextView*/
    private TextView nEmptyStateTextView;

    private ProgressBar loadingIndicator;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);

        //**Reference a listview in the layout*/
        ListView newsListView = (ListView) findViewById(R.id.listview_news);

        //**Create the adapter that takes an empty list of articles as input*/
        nAdapter = new NewsAdapter(this, new ArrayList<News>());

        //**Set adapter on the list view*/
        newsListView.setAdapter(nAdapter);

     //   SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
       // new SharedPreferences.OnSharedPreferenceChangeListener(this);

        try{
            nEmptyStateTextView = findViewById(R.id.empty_view);
            newsListView.setEmptyView(nEmptyStateTextView);

        //**Check state of network connectivity*/
        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            android.app.LoaderManager loaderManager = getLoaderManager();
            if (loaderManager.getLoader ( NEWS_LOADER_ID ) != null) {
                loaderManager.restartLoader ( NEWS_LOADER_ID, null, this );
            } else {
                loaderManager.initLoader ( NEWS_LOADER_ID, null, this );
            }
        } else {
            loadingIndicator = findViewById ( R.id.loading_indicator );
            loadingIndicator.setVisibility ( View.GONE );

            // set empty state to display "No internet connection."
            nEmptyStateTextView.setText ( R.string.no_internet_connection );
        }
    } catch (NullPointerException npe) {
        Log.e ( "NewsActivity", "No internet connection", npe );
    }




        //**Set a click listener on each item*/
        newsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {

                //**Find the article clicked on*/
                News currentNews = nAdapter.getItem(position);

                try {
                    //**Convert the string URL to an object Uri*/
                    Uri newsUri = Uri.parse(currentNews.getUrl());

                    //**Create an intent to view the full article*/
                    Intent websiteIntent = new Intent(Intent.ACTION_VIEW, newsUri);

                    //**Launch a new activity*/
                    startActivity(websiteIntent);
                } catch (NullPointerException npe) {
                    Log.e("error parsing", "News Activity");
                }
            }
        });}

      //  **Get a reference to loader manager*/
  //   LoaderManager loaderManager = getLoaderManager();

            //**Initialize Loader*//
    //        loaderManager.initLoader(NEWS_LOADER_ID, null, this);
    //    }else {
    //        View loadingIndicator = findViewById(R.id.loading_indicator);
    //        loadingIndicator.setVisibility(View.GONE);

         //   nEmptyStateTextView.setText(R.string.no_internet_connection);
       // }}


//   @Override
 //  public void onSharedPreferenceChanged(SharedPreferences preferences, String key) {
 //      if (key.equals(getString(R.string.settings_order_by_key)) || key.equals(getString(R.string.settings_order_by_label))){
   //        nAdapter.clear();

     //      nEmptyStateTextView.setVisibility(View.GONE);

       //     View loadingIndicator = findViewById(R.id.loading_indicator);
         //  loadingIndicator.setVisibility(View.VISIBLE);

//            getLoaderManager().restartLoader(NEWS_LOADER_ID, null, this);
  //     }

 //  }

    @Override
    public Loader<List<News>>onCreateLoader(int i, Bundle bundle) {

            SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
//        return new NewsLoader(this, NEWS_QUERY_URL);


         String sectionId = sharedPrefs.getString(
          getString(R.string.settings_section_id_key),
        getString(R.string.settings_section_id_default));

       Uri baseUri = Uri.parse(NEWS_QUERY_URL);

        Uri.Builder uriBuilder =baseUri.buildUpon();


     uriBuilder.appendQueryParameter("format", "json");
     uriBuilder.appendQueryParameter("limit", "10");
//     uriBuilder.appendQueryParameter("sectionId",sectionid);
        return new NewsLoader(this, uriBuilder.toString());
   }

    @Override
    public void onLoadFinished(Loader<List<News>> loader, List<News> news) {
          View loadingIndicator = findViewById(R.id.loading_indicator);
          loadingIndicator.setVisibility(View.GONE);

        //**Clear Adapter*/
        nAdapter.clear();

        nEmptyStateTextView.setText(R.string.no_news);

        if (news != null && !news.isEmpty()) {
            nAdapter.addAll(news);

        }}
        @Override
        public void onLoaderReset (Loader<List<News >> loader) {
            nAdapter.clear();
        }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            Intent settingsIntent = new Intent(this, SettingsActivity.class);
            startActivity(settingsIntent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}

