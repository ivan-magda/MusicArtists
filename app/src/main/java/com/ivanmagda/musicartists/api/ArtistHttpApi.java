package com.ivanmagda.musicartists.api;


import android.util.Log;

import com.ivanmagda.musicartists.model.Artist;
import com.ivanmagda.musicartists.model.Cover;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ArtistHttpApi {

    // Properties.

    private static String LOG_TAG = ArtistHttpApi.class.getSimpleName();

    // General JSON response keys.
    private static final String ID_KEY = "id";
    private static final String NAME_KEY = "name";
    private static final String GENRES_KEY = "genres";
    private static final String TRACKS_KEY = "tracks";
    private static final String ALBUMS_KEY = "albums";
    private static final String LINK_KEY = "link";
    private static final String DESCRIPTION_KEY = "description";

    // Cover JSON response keys.
    private static final String COVER_KEY = "cover";
    private static final String COVER_SMALL_KEY = "small";
    private static final String COVER_Big_KEY = "big";

    public static List<Artist> getArtists() {
        return parseArtists(HttpAPI.execute("mobilization-2016/artists.json"));
    }

    // Parsing JSON data.

    protected static List<Artist> parseArtists(String rawData) {
        List<Artist> artists = null;

        try {
            JSONArray jsonArtistsArray = new JSONArray(rawData);
            artists = new ArrayList<>(jsonArtistsArray.length());

            for (int i = 0; i < jsonArtistsArray.length(); i++) {
                // Get JSON item object at specific index.
                JSONObject jsonArtist = jsonArtistsArray.getJSONObject(i);
                artists.add(parseArtist(jsonArtist));
            }

            Log.d(LOG_TAG, "Successfully parsed " + artists.size() + " artists.");
        } catch (JSONException jsonError) {
            jsonError.printStackTrace();
            Log.e(LOG_TAG, "Failed to processing the JSON data");
        }

        return artists;
    }

    protected static Artist parseArtist(JSONObject jsonObject) {
        Artist artist = null;

        try {
            // Access values from the fields.
            long id = jsonObject.getInt(ID_KEY);
            String name = jsonObject.getString(NAME_KEY);
            int tracks = jsonObject.getInt(TRACKS_KEY);
            int albums = jsonObject.getInt(ALBUMS_KEY);
            String description = jsonObject.getString(DESCRIPTION_KEY);

            // Artist may do not have a link to the personal site.
            String link = null;
            if (jsonObject.has(LINK_KEY)) {
                link = jsonObject.getString(LINK_KEY);
            }

            // Get genres.
            JSONArray genresArray = jsonObject.getJSONArray(GENRES_KEY);
            String[] genres = new String[genresArray.length()];
            for (int i = 0; i < genresArray.length(); i++) {
                genres[i] = genresArray.getString(i);
            }

            // Get cover.
            JSONObject coverObject = jsonObject.getJSONObject(COVER_KEY);
            String coverSmall = coverObject.getString(COVER_SMALL_KEY);
            String coverBig = coverObject.getString(COVER_Big_KEY);
            Cover cover = new Cover(coverSmall, coverBig);

            artist = new Artist(id, name, genres, tracks, albums, link, description, cover);
        } catch (JSONException jsonError) {
            jsonError.printStackTrace();
            Log.e(LOG_TAG, "Failed to processing JSON data");
        }

        return artist;
    }

}
