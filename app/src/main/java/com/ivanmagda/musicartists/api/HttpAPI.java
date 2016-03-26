package com.ivanmagda.musicartists.api;

import android.content.Context;
import android.util.Log;

import com.ivanmagda.musicartists.R;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

// TODO: research on caching requests.
// TODO: timeout settings for connectios.
// TODO: initialize when the application is launching.

public class HttpAPI {

    // Properties.

    /**
     * Log tag for debug statements.
     */
    private static String LOG_TAG = HttpAPI.class.getSimpleName();

    private static String baseURL;

    // Methods.

    public static void init(Context appContext) {
        baseURL = appContext.getString(R.string.http_api_url);
    }

    protected static String execute(String uri) {
        HttpURLConnection connection = null;

        try {
            URL url = new URL(baseURL + uri);

            // Getting a connection to the resource referred to by this URL
            // and trying to connect.
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
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
            if (reader != null) {
                try {
                    reader.close();
                } catch (final IOException exception) {
                    Log.e(LOG_TAG, "Failed to close the stream", exception);
                }
            }
        }

        return null;
    }
}
