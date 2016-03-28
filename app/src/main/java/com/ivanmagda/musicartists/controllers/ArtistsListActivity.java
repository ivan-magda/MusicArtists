package com.ivanmagda.musicartists.controllers;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.ivanmagda.musicartists.R;
import com.ivanmagda.musicartists.fragments.ArtistsListFragment;
import com.ivanmagda.musicartists.model.Artist;

// TODO: Use fragment for async artists downloading.

public class ArtistsListActivity extends BaseActivity implements ArtistsListFragment.OnArtistSelected {

    // Properties.

    private static final String LOG_TAG = ArtistsListActivity.class.getSimpleName();

    // Life Cycle.

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_artists_list);
        activateToolbar();

        if (savedInstanceState == null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.root_layout, ArtistsListFragment.newInstance(), "artistList")
                    .commit();
        }
    }

    // OnArtistSelected.

    @Override
    public void onArtistSelected(Artist artist) {
        Intent detailIntent = new Intent(ArtistsListActivity.this, ArtistDetailActivity.class);
        detailIntent.putExtra(ARTIST_TRANSFER, artist);
        startActivity(detailIntent);

        Log.d(LOG_TAG, "Did select " + artist.getName());
    }

}
