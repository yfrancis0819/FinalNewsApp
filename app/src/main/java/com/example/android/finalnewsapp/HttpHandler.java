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

            //**Looping through all articles*/
            for(int i = 0; i < newsArray.length(); i++) {
                JSONObject properties = newsArray.getJSONObject(i);


                String section = properties.getString("sectionName");
                String webTitle = properties.getString("webTitle");
                String webUrl = properties.getString("webUrl");
                String webPublicationDate = properties.getString("webPublicationDate");




                News news1 = new News(webTitle, section, webPublicationDate, webUrl);
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





