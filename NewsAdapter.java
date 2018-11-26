package com.example.android.finalnewsapp;

import android.app.Activity;
import android.content.Context;
import android.content.AsyncTaskLoader;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class   NewsAdapter extends ArrayAdapter<News> {

    public NewsAdapter(Activity context,ArrayList<News> news){
        super (context, 0, news);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        final ViewHolder holder;
        try {
            if (convertView == null) {
                convertView = LayoutInflater.from ( getContext () ).inflate ( R.layout.list_item, parent, false );
                holder = new ViewHolder ( convertView );
                convertView.setTag ( holder );
            } else {
                holder = (ViewHolder) convertView.getTag ();
            }
            if (position < getCount ()) {
                News currentNews = getItem ( position );

                holder.sectionTextView.setText ( currentNews.getSection() );
                holder.titleTextView.setText ( currentNews.getTitle());
                holder.dateTextView.setText ( formatDate ( currentNews.getDate() ) );
                holder.authorTextView.setText ( currentNews.getAuthor() );
            }
        } catch (NullPointerException npe) {
            Log.e ( "NewsAdapter", "getSection() throws npe", npe );
        }
        return convertView;
    }

    static class ViewHolder {
        private TextView sectionTextView;
        private TextView titleTextView;
        private TextView dateTextView;
        private TextView authorTextView;

        ViewHolder(View view) {
            sectionTextView = view.findViewById ( R.id.section );
            titleTextView = view.findViewById ( R.id.title );
            dateTextView = view.findViewById ( R.id.date );
            authorTextView = view.findViewById ( R.id.author );
        }
    }

    /* date string, example format: Jan 09, 2018 10:05:08 AM */
    private String formatDate(String date) {
        Date dateObject = new Date ();
        try {

            SimpleDateFormat simpleDateFormat = new SimpleDateFormat ( "yyyy-MM-dd'T'kk:mm:ss'Z'", Locale.US );
            dateObject = simpleDateFormat.parse ( date );
        } catch (ParseException e) {
            e.printStackTrace ();
        }
        SimpleDateFormat newDateFormat = new SimpleDateFormat ( "MMM dd, yyyy hh:mm:ss a", Locale.US );
        String dateFormatted = newDateFormat.format ( dateObject );
        return dateFormatted;
    }

        public static class NewsLoader extends AsyncTaskLoader<List<News>> {

            private String url;

            public NewsLoader (Context context, String url){
                super(context);
                    this.url = url;
            }

            @Override
            protected void onStartLoading() {
                forceLoad();
            }
            @Override

            public List<News> loadInBackground() {
                if (url == null){
                    return null;
                    }

                 List<News> news = HttpHandler.fetchNewsData(url);
                 return news;
            }
        }
    }
