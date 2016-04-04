package com.ivanmagda.musicartists.ui.controllers;

import android.os.Bundle;

import com.ivanmagda.musicartists.R;
import com.ivanmagda.musicartists.ui.fragments.ArtistsListFragment;

public class ArtistsListActivity extends BaseActivity {

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

}
