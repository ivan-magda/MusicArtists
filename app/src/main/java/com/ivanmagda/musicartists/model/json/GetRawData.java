package com.ivanmagda.musicartists.model.json;

import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Got all the states that is class is gonna to be in, all the possobilities.
 * Use for GetRawData states tracking.
 *
 * IDLE - not processing anything.
 * PROCESSING - is in progress, downloading the data.
 * NOT_INITIALIZED - haven't got any valid URL for to be in download state.
 * FAILED_OR_EMPTY - failed to download the data or data comeback empty.
 * OK - got valid data.
 */
enum DownloadStatus {
    IDLE,
    PROCESSING,
    NOT_INITIALIZED,
    FAILED_OR_EMPTY,
    OK
}

/**
 * Base class for downloading raw(unprocessed JSON) data from the passed in URL.
 */
public class GetRawData {

    // Properties.

    /**
     * Log tag for debug statements.
     */
    private  String LOG_TAG = GetRawData.class.getSimpleName();

    /**
     * URL from where data will be downloading.
     */
    private String rawURL;


    /**
     * Returned data, after when downloading is completed.
     */
    private String data;


    /**
     * State in which class is in now.
     */
    private DownloadStatus downloadStatus;

    // Initializers.

    public GetRawData(String rawURL) {
        this.rawURL = rawURL;
        this.downloadStatus = DownloadStatus.IDLE;
    }

    // Getters.

    public String getData() {
        return data;
    }

    public DownloadStatus getDownloadStatus() {
        return downloadStatus;
    }

    // Setters.

    public void setRawURL(String rawURL) {
        this.rawURL = rawURL;
    }

    // Methods.

    /**
     * Returns the object to the initial state.
     */
    public void reset() {
        this.downloadStatus = DownloadStatus.IDLE;
        this.rawURL = null;
        this.data = null;
    }


    /**
     * Executes process for data downloading from the passed URL.
     */
    public void execute() {
        downloadStatus = DownloadStatus.PROCESSING;

        DownloadRawData downloadRawData = new DownloadRawData();
        downloadRawData.execute(rawURL);
    }

    // DownloadRawData extends AsyncTask.

    /**
     * Class for the asynchronous raw data downloading.
     */
    public class DownloadRawData extends AsyncTask<String, Void, String> {
        @Override
        protected void onPostExecute(String responseData) {
            super.onPostExecute(responseData);

            data = responseData;
            Log.v(LOG_TAG, "Returned data is: " + data);

            if (data == null) {
                if (rawURL == null) {
                    downloadStatus = DownloadStatus.NOT_INITIALIZED;
                } else {
                    downloadStatus = DownloadStatus.FAILED_OR_EMPTY;
                }
            } else {
                downloadStatus = DownloadStatus.OK;
            }
        }

        @Override
        protected String doInBackground(String... params) {
            HttpURLConnection connection = null;
            BufferedReader reader = null;

            // Check for the passed in parameters.
            // Return immediately if not got valid parameters.
            if (params == null) {
                return null;
            }

            try {
                URL url = new URL(params[0]);

                // Getting a connection to the resource referred to by this URL
                // and trying to connect.
                connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                connection.connect();

                InputStream inputStream = connection.getInputStream();
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
                Log.e(LOG_TAG, "Failed to download raw data", exception);
                return null;
            } finally {
                if (connection != null) {
                    connection.disconnect();
                }

                if (reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException exception) {
                        Log.e(LOG_TAG, "Failed to close the stream", exception);
                    }
                }
            }
        }
    }

}
