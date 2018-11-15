package com.example.android.finalnewsapp;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class NewsAdapter extends ArrayAdapter<News> {


    public static final String Log_Tag = NewsActivity.class.getName();

    public NewsAdapter(Activity context, ArrayList<News> articles){
        super (context, 0, articles);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View listItemView = convertView;
        if (listItemView == null){
            listItemView = LayoutInflater.from(getContext()).inflate(R.layout.list_item, parent, false);
        }

        News currentArticle = getItem(position);

        TextView titleTextView = (TextView) listItemView.findViewById(R.id.title);
        titleTextView.setText(currentArticle.getTitle());

        TextView sectionTextView = (TextView) listItemView.findViewById(R.id.section);
        sectionTextView.setText(currentArticle.getSectionID());

        TextView dateTextView = (TextView) listItemView.findViewById(R.id.date);
        dateTextView.setText(currentArticle.getDate());

        return listItemView;

    }
}