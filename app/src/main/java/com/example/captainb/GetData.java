package com.example.captainb;

import android.os.AsyncTask;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

public class GetData extends AsyncTask <URL,Void,String> {

    private static final String TAG = "GetData";

    protected String getResponseHttp(URL url) throws IOException {
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try {
            InputStream in = urlConnection.getInputStream();
            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");
            boolean hasInput = scanner.hasNext();
            String result;
            if(hasInput) {
                result = scanner.next();
                return result;
            } else {
                return null;
            }
        } finally {
            urlConnection.disconnect();
        }
    }

    @Override
    protected void onPreExecute() {
        Log.d(TAG, "onPreExecute 1: ");
        super.onPreExecute();
    }

    @Override
    protected String doInBackground(URL[] url) {
        Log.d(TAG, "doInBackground: ");

        URL urlQuery = url[0];
        String result = null;
        try {
            result = getResponseHttp(urlQuery);

        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    protected void onPostExecute(String result) {
        Log.d(TAG, "onPostExecute: ");
        Log.d(TAG, "onPostExecute: " + result);
//        super.onPostExecute(result);
    }
}
