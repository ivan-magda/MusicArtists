package com.ivanmagda.musicartists.controllers;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.ivanmagda.musicartists.R;
import com.ivanmagda.musicartists.model.Artist;
import com.squareup.picasso.Picasso;

public class ArtistDetailActivity extends BaseActivity {

    // Activity Life Cycle.

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_artist_detail);
        activateToolbarWithHomeEnabled();

        Intent intent = getIntent();
        Artist artist = intent.getParcelableExtra(ARTIST_TRANSFER);

        configureUIWithArtist(artist);
    }

    // Helpers.

    private void configureUIWithArtist(Artist artist) {
        setTitle(artist.getName());

        TextView genresTextView = (TextView) findViewById(R.id.genres_detail_text_view);
        TextView summaryTextView = (TextView) findViewById(R.id.summary_detail_text_view);
        TextView biographyTextView = (TextView) findViewById(R.id.description_detail_text_view);
        ImageView imageView = (ImageView) findViewById(R.id.cover_detail_image_view);

        assert genresTextView != null && summaryTextView != null && biographyTextView != null;

        // Artist genres.
        genresTextView.setText(artist.getGenresString());

        // Artist summary.
        String summary = artist.getAlbumsSummary(this) + "    ·    " + artist.getTracksSummary(this);
        summaryTextView.setText(summary);

        // Biography with capitalize first letter.
        StringBuilder biography = new StringBuilder(artist.getDescription());
        biography.setCharAt(0, Character.toUpperCase(biography.charAt(0)));
        biographyTextView.setText(biography.toString());

        // Load an image.
        Picasso.with(this)
                .load(artist.getCover().getBig())
                .error(R.drawable.placeholder)
                .placeholder(R.drawable.placeholder)
                .into(imageView);
    }

}
