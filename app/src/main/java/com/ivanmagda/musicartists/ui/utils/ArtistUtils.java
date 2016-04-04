package com.ivanmagda.musicartists.ui.utils;

import android.content.Context;

import com.ivanmagda.musicartists.R;
import com.ivanmagda.musicartists.model.Artist;

public class ArtistUtils {

    public static String buildGenresArtistDescription(Artist artist) {
        String[] genres = artist.getGenres();

        if (genres == null || genres.length == 0) {
            return null;
        }

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(genres[0]);

        for (int i = 1; i < genres.length; i++) {
            stringBuilder.append(", ");
            stringBuilder.append(genres[i]);
        }

        return stringBuilder.toString();
    }

    static public String buildArtistAlbumsSummary(Artist artist, Context context) {
        int albums = artist.getAlbums();
        return albums + " " + getProperWordCompletion(albums,
                context.getString(R.string.album_single),
                context.getString(R.string.album_several),
                context.getString(R.string.album_many));
    }

    static public String buildArtistTracksSummary(Artist artist, Context context) {
        int tracks = artist.getTracks();
        return tracks + " " + getProperWordCompletion(tracks,
                context.getString(R.string.track_single),
                context.getString(R.string.track_several),
                context.getString(R.string.track_many));
    }

    // TODO: http://developer.android.com/intl/ru/guide/topics/resources/string-resource.html#Plurals

    /**
     * Proper completion of words or numbers and words, taking into Russian morphology.
     *
     * @param number  - amount value
     * @param single  - singular word string.
     * @param several - several word string.
     * @param many    - many word string.
     * @return - correct word based on Russian morphology.
     */
    static private String getProperWordCompletion(int number, String single, String several, String many) {
        int value = number % 100;

        if (value > 10 && value < 20) {
            return many;
        } else {
            value = number % 10;

            if (value == 1) {
                return single;
            } else if (value > 1 && value < 5) {
                return several;
            } else {
                return many;
            }
        }
    }

}
