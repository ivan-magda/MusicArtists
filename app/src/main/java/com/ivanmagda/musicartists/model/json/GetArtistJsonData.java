package com.ivanmagda.musicartists.model.json;


import android.util.Log;

import com.ivanmagda.musicartists.model.Artist;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Load and processing artist JSON data over the raw data.
 * Inherit and reuse functionality from the GetRawData class.
 */
public class GetArtistJsonData extends GetRawData {

    // Properties.

    /**
     * Log tag for debug statements.
     */
    private String LOG_TAG = GetArtistJsonData.class.getSimpleName();

    /**
     * Destination URL string, from where the artist data will be downloading.
     */
    private String destinationURL;


    /**
     * Downloaded artists from the destination url.
     */
    private List<Artist> artists;

    // Initializers.

    public GetArtistJsonData(String destinationURL) {
        super(destinationURL);
        this.destinationURL = destinationURL;
        artists = new ArrayList<>();
    }

    public void processResult() {
        // Get a successful download status?
        if(getDownloadStatus() != DownloadStatus.OK) {
            Log.e(LOG_TAG, "Failed to download raw data");
            return;
        }

        // Start parsing the data.
        try {
            JSONArray jsonArtistsArray = new JSONArray(getData());

            // Process on JSON array.
            int length = jsonArtistsArray.length();
            for (int i = 0; i < length; i++) {
                // Get JSON item object at specific index.
                JSONObject jsonArtist = jsonArtistsArray.getJSONObject(i);

                // Create Artist object from the JSONObject and append it to the artists array.
                Artist artist = new Artist(jsonArtist);
                artists.add(artist);
            }

            Log.d(LOG_TAG, "Successfully parsed " + artists.size() + " artists.");
        } catch (JSONException jsonError) {
            jsonError.printStackTrace();
            Log.e(LOG_TAG, "Failed to processing the JSON data");
        }
    }

    // Methods.

    public void execute() {
        // Set raw url manually to avoid cases where the url isn't going to be set.
        super.setRawURL(destinationURL);

        DownloadJsonData downloadJsonData = new DownloadJsonData();
        Log.v(LOG_TAG, "Will be downloading from: " + destinationURL);
        downloadJsonData.execute(destinationURL);
    }

    // DownloadJsonData extends DownloadRawData.

    /**
     * First download raw data and only once.
     * When it's being downloaded start process it as JSON data.
     *
     * Inherit async task functionality from the DownloadRawData to do that.
     */
    public class DownloadJsonData extends DownloadRawData {
        @Override
        protected void onPostExecute(String responseData) {
            super.onPostExecute(responseData);
            processResult();
        }

        @Override
        protected String doInBackground(String... params) {
            String[] param = {destinationURL};
            return super.doInBackground(param);
        }
    }
}
