package com.ivanmagda.musicartists.model;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

import com.ivanmagda.musicartists.R;

import java.util.Arrays;

/**
 * Represents music artist.
 */
public class Artist implements Parcelable, Comparable<Artist> {

    // Properties.

    private static String LOG_TAG = Artist.class.getSimpleName();

    private long id;
    private String name;
    private String[] genres;
    private int tracks;
    private int albums;
    private String link;
    private String description;
    private Cover cover;

    // Initializers.

    public Artist(long id, String name, String[] genres, int tracks, int albums, String link, String description, Cover cover) {
        this.id = id;
        this.name = name;
        this.genres = genres;
        this.tracks = tracks;
        this.albums = albums;
        this.link = link;
        this.description = description;
        this.cover = cover;
    }

    protected Artist(Parcel in) {
        id = in.readLong();
        name = in.readString();
        genres = in.createStringArray();
        tracks = in.readInt();
        albums = in.readInt();
        link = in.readString();
        description = in.readString();
        cover = in.readParcelable(Cover.class.getClassLoader());
    }

    // Getters.

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String[] getGenres() {
        return genres;
    }

    public String getGenresString() {
        if (genres.length == 0) {
            return "(Undefined)";
        }

        StringBuilder stringBuilder = new StringBuilder();

        int length = genres.length;
        for (int i = 0; i < length; i++) {
            if (i == 0) {
                stringBuilder.append(genres[i]);
                if (length != 1) {
                    stringBuilder.append(",");
                }
            } else if (i == length - 1) {
                stringBuilder.append(" ").append(genres[i]);
            } else {
                stringBuilder.append(" ").append(genres[i]).append(",");
            }
        }

        return stringBuilder.toString();
    }

    public int getTracks() {
        return tracks;
    }

    public int getAlbums() {
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

    // Helpers.

    public String getAlbumsSummary(Context context) {
        return getAlbums() + " " + getCorrectWord(getAlbums(),
                context.getString(R.string.album_single),
                context.getString(R.string.album_several),
                context.getString(R.string.album_many));
    }

    public String getTracksSummary(Context context) {
        return getTracks() + " " + getCorrectWord(getTracks(),
                context.getString(R.string.track_single),
                context.getString(R.string.track_several),
                context.getString(R.string.track_many));
    }

    /**
     * Proper completion of words or numbers and words, taking into Russian morphology.
     *
     * @param number  - amount value
     * @param single  - singular word string.
     * @param several - several word string.
     * @param many    - many word string.
     * @return - correct word based on Russian morphology.
     */
    private String getCorrectWord(int number, String single, String several, String many) {
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

    // Parcelable.

    public static final Creator<Artist> CREATOR = new Creator<Artist>() {
        @Override
        public Artist createFromParcel(Parcel in) {
            return new Artist(in);
        }

        @Override
        public Artist[] newArray(int size) {
            return new Artist[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeString(name);
        dest.writeStringArray(genres);
        dest.writeInt(tracks);
        dest.writeInt(albums);
        dest.writeString(link);
        dest.writeString(description);
        dest.writeParcelable(cover, flags);
    }

    // Comparable.

    @Override
    public int compareTo(@NonNull Artist another) {
        return this.name.compareTo(another.getName());
    }

}
