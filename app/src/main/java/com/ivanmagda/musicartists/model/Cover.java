package com.ivanmagda.musicartists.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Represents cover image of the artist.
 * <p>
 * Maybe be in two resolutions: small and big.
 * Small is 300x300px.
 * Big is 1000x1000px.
 */
public class Cover implements Parcelable {

    // Properties.

    private String small;
    private String big;

    // Initializers.

    public Cover(String small, String big) {
        this.small = small;
        this.big = big;
    }

    // Getters.

    protected Cover(Parcel in) {
        small = in.readString();
        big = in.readString();
    }

    public String getSmall() {
        return small;
    }

    public String getBig() {
        return big;
    }

    // Parcelable.

    public static final Creator<Cover> CREATOR = new Creator<Cover>() {
        @Override
        public Cover createFromParcel(Parcel in) {
            return new Cover(in);
        }

        @Override
        public Cover[] newArray(int size) {
            return new Cover[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(small);
        dest.writeString(big);
    }

}
