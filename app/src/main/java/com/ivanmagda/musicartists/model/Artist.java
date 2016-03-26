package com.ivanmagda.musicartists.model;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

interface ArtistJSONResponseKey {
    // General.
    String Id = "id";
    String Name = "name";
    String Genres = "genres";
    String Tracks = "tracks";
    String Albums = "albums";
    String Link = "link";
    String Description = "description";

    // Cover.
    String Cover = "cover";
    String CoverSmall = "small";
    String CoverBig = "big";
}

/**
 * Represents music artist.
 */
public class Artist {

    // Properties.

    private String LOG_TAG = Artist.class.getSimpleName();

    private Integer id;
    private String name;
    private String[] genres;
    private Integer tracks;
    private Integer albums;
    private String link = null;
    private String description;
    private Cover cover;

    // Initializers.

    public Artist(Integer id, String name, String[] genres, Integer tracks, Integer albums, String link, String description, Cover cover) {
        this.id = id;
        this.name = name;
        this.genres = genres;
        this.tracks = tracks;
        this.albums = albums;
        this.link = link;
        this.description = description;
        this.cover = cover;
    }

    public Artist(JSONObject jsonArtist) {
        try {
            // Access values from the fields.
            id = jsonArtist.getInt(ArtistJSONResponseKey.Id);
            name = jsonArtist.getString(ArtistJSONResponseKey.Name);
            tracks = jsonArtist.getInt(ArtistJSONResponseKey.Tracks);
            albums = jsonArtist.getInt(ArtistJSONResponseKey.Albums);
            description = jsonArtist.getString(ArtistJSONResponseKey.Description);

            // Artist may do not have a link to the personal site.
            if (jsonArtist.has(ArtistJSONResponseKey.Link)) {
                link = jsonArtist.getString(ArtistJSONResponseKey.Link);
            }

            // Get genres.
            List<String> genresList = new ArrayList<>();
            JSONArray genresArray = jsonArtist.getJSONArray(ArtistJSONResponseKey.Genres);
            int length = genresArray.length();
            for (int i = 0; i < length; i++) {
                genresList.add(genresArray.getString(i));
            }
            // Convert ArrayList<String> to String[] array.
            genres = new String[genresList.size()];
            genres = genresList.toArray(genres);

            // Get cover.
            JSONObject coverObject = jsonArtist.getJSONObject(ArtistJSONResponseKey.Cover);
            String coverSmall = coverObject.getString(ArtistJSONResponseKey.CoverSmall);
            String coverBig = coverObject.getString(ArtistJSONResponseKey.CoverBig);
            cover = new Cover(coverSmall, coverBig);
        } catch (JSONException jsonError) {
            jsonError.printStackTrace();
            Log.e(LOG_TAG, "Failed to processing JSON data");
        }
    }

    // Getters.

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String[] getGenres() {
        return genres;
    }

    public Integer getTracks() {
        return tracks;
    }

    public Integer getAlbums() {
        return albums;
    }

    public String getLink() {
        return link;
    }

    public String getDescription() {
        return description;
    }

    public Cover getCover() {
        return cover;
    }

    @Override
    public String toString() {
        return "Artist{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", genres=" + Arrays.toString(genres) +
                ", tracks=" + tracks +
                ", albums=" + albums +
                ", link='" + link + '\'' +
                ", description='" + description + '\'' +
                ", cover=" + cover +
                '}';
    }

}
