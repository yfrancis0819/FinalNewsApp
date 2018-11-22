package com.example.android.finalnewsapp;

import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

public class HttpHandler {
    private static final String LOG_TAG = HttpHandler.class.getSimpleName();
    private static final int INDEX_ZERO = 0;
    private static final int INDEX_ONE= 1;
    private static final int INDEX_TWO = 2;
    private static final int INDEX_THREE = 3;

    private HttpHandler() {
    }
    private static List<News> extractFeatureFromJson(String newsJSON) {
        if (TextUtils.isEmpty ( newsJSON )) {
            return null;
        }

       List<News> news = new ArrayList<>();
        try{
            JSONObject baseJsonResponse = new JSONObject(newsJSON);
            JSONObject response = baseJsonResponse.getJSONObject("response");
            JSONArray newsArray = response.getJSONArray("results");

            /**Looping through all articles*/
            for(int i = 0; i < newsArray.length(); i++) {
                JSONObject currentNews = newsArray.getJSONObject(i);

                String section = currentNews.getString("sectionName");
                String webTitle = currentNews.getString("webTitle");
                String webUrl = currentNews.getString("webUrl");
                String webPublicationDate = currentNews.getString("webPublicationDate");
                StringBuilder author = new StringBuilder ( "By: " );
                JSONArray authorArray = currentNews.getJSONArray ( "tags" );

                if (authorArray != null && authorArray.length () > INDEX_ZERO) {

                    for (int a = INDEX_ZERO; a < authorArray.length (); a++) {
                        JSONObject authors = authorArray.getJSONObject ( a );

                        String authorsListed = authors.optString ( "webTitle" );
                        if (authorArray.length () > INDEX_ONE) {
                            author.append ( authorsListed );
                            author.append ( "\t\t\t" );

                        } else {
                            author.append ( authorsListed );
                        }
                    }
                } else {
                    author.replace ( INDEX_ZERO, INDEX_THREE, "No author(s) listed" );
                }

                News news1 = new News(webTitle, section, webPublicationDate, webUrl, author.toString());
                news.add(news1);
            }

        } catch (JSONException e) {

            Log.e("HttpHandler", "Problem parsing the News JSON results", e);
        }

        return news;
    }

    private static URL createUrl(String stringUrl) {
        URL url = null;
        try{
            url = new URL(stringUrl);
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Error with creating URL ", e);
        }
        return url;
    }


    private static String makeHttpRequest(URL url) throws IOException {
        String jsonResponse = "";

        if(url == null){
            return jsonResponse;
        }
        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(10000);
            urlConnection.setConnectTimeout(15000);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            if (urlConnection.getResponseCode() ==200){
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else {
                Log.e(LOG_TAG, "Error response code: " + urlConnection.getResponseCode());
            }
        } catch (IOException e) {
            Log.e ( LOG_TAG, "Problem retrieving the news JSON results.", e );
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                inputStream.close();
            }
        }
        return jsonResponse;
    }

    private static String readFromStream(InputStream inputStream) throws IOException  {
        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }

        return output.toString();
    }


    public static List<News> fetchNewsData(String requestUrl){

        URL url = createUrl(requestUrl);

        String jsonResponse = null;

        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Error closing input stream", e);
        }

        List<News> news = extractFeatureFromJson(jsonResponse);

        return news;
}
}





