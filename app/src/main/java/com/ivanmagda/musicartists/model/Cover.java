package com.ivanmagda.musicartists.model;

/**
 * Represents cover image of the artist.
 *
 * Maybe be in two resolutions: small and big.
 * Small is 300x300px.
 * Big is 1000x1000px.
 */
public class Cover {

    // Properties.

    private String small;
    private String big;

    // Initializers.

    public Cover(String small, String big) {
        this.small = small;
        this.big = big;
    }

    // Getters.

    public String getSmall() {
        return small;
    }

    public String getBig() {
        return big;
    }

}
