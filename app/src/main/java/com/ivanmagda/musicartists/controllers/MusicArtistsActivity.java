package com.ivanmagda.musicartists.controllers;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

import com.ivanmagda.musicartists.R;
import com.ivanmagda.musicartists.api.ArtistHttpApi;
import com.ivanmagda.musicartists.model.Artist;
import com.ivanmagda.musicartists.view.DividerItemDecoration;
import com.ivanmagda.musicartists.view.MusicArtistRecyclerViewAdapter;

import java.util.ArrayList;
import java.util.List;

public class MusicArtistsActivity extends BaseActivity {

    // Properties.

    private static final String LOG_TAG = MusicArtistsActivity.class.getSimpleName();

    private List<Artist> artistList = new ArrayList<>();
    private RecyclerView recyclerView;
    private MusicArtistRecyclerViewAdapter musicArtistRecyclerViewAdapter;

    // Getters/Setters.

    public List<Artist> getArtistList() {
        return artistList;
    }

    public void setArtistList(List<Artist> artistList) {
        this.artistList = artistList;
    }

    // Activity Life Cycle.

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        activateToolbar();

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addItemDecoration(new DividerItemDecoration(this, R.drawable.divider));

        // Check the Network Connection.
        if (ArtistHttpApi.isOnline(this)) {
            new DownloadMusicArtistsTask().execute();
        } else {
            Toast.makeText(MusicArtistsActivity.this, "You are offline. Please connect to the internet.",
                    Toast.LENGTH_SHORT).show();
        }
    }

    // DownloadMusicArtistsTask.

    private class DownloadMusicArtistsTask extends AsyncTask<String, Void, List<Artist>> {
        @Override
        protected List<Artist> doInBackground(String... params) {
            return ArtistHttpApi.getArtists();
        }

        @Override
        protected void onPostExecute(List<Artist> artists) {
            super.onPostExecute(artists);

            setArtistList(artists);
            Log.d(LOG_TAG, "Fetched " + getArtistList().size() + " artists");

            musicArtistRecyclerViewAdapter = new MusicArtistRecyclerViewAdapter(MusicArtistsActivity.this,
                    getArtistList());
            recyclerView.setAdapter(musicArtistRecyclerViewAdapter);
        }

    }

}
