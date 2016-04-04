package com.ivanmagda.musicartists.api;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

// TODO: cache http requests using HttpResponseCache.

public class HttpAPI {

    // Properties.

    /**
     * Log tag for debug statements.
     */
    private static String LOG_TAG = HttpAPI.class.getSimpleName();

    // Network.

    public static boolean isOnline(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected();
    }

    protected static String execute(String requestURL) {
        // Getting a connection to the resource referred to by this URL
        // and trying to connect.
        HttpURLConnection connection = null;

        try {
            URL url = new URL(requestURL);

            connection = (HttpURLConnection) url.openConnection();
            connection.setReadTimeout(10000);
            connection.setConnectTimeout(15000);
            connection.setRequestMethod("GET");
            connection.setDoInput(true);

            // Starts the query.
            connection.connect();

            return processOnResponse(connection);
        } catch (IOException exception) {
            Log.e(LOG_TAG, "Failed to download raw data", exception);
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }

        return null;
    }

    private static String processOnResponse(HttpURLConnection connection) {
        BufferedReader reader = null;
        InputStream inputStream = null;

        try {
            inputStream = connection.getInputStream();

            if (inputStream == null) {
                return null;
            }

            // Will be storing response data.
            StringBuilder stringBuilder = new StringBuilder();

            reader = new BufferedReader((new InputStreamReader(inputStream)));

            // Reading each line of the data.
            String line;
            while ((line = reader.readLine()) != null) {
                stringBuilder.append(line).append("\n");
            }

            return stringBuilder.toString();
        } catch (IOException exception) {
            Log.e(LOG_TAG, "Failed to process on http response", exception);
        } finally {
            try {
                if (reader != null) {
                    reader.close();
                }

                if (inputStream != null) {
                    inputStream.close();
                }
            } catch (IOException exception) {
                Log.e(LOG_TAG, "Failed to close", exception);
            }
        }

        return null;
    }
}
