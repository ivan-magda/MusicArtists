package com.ivanmagda.musicartists.controllers;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.ivanmagda.musicartists.R;
import com.ivanmagda.musicartists.view.RecyclerItemClickListener;
import com.ivanmagda.musicartists.api.ArtistHttpApi;
import com.ivanmagda.musicartists.model.Artist;
import com.ivanmagda.musicartists.view.DividerItemDecoration;
import com.ivanmagda.musicartists.view.MusicArtistRecyclerViewAdapter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

// TODO: Use fragment for async artists downloading.

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

        configureUI();

        // Check the Network Connection.
        if (ArtistHttpApi.isOnline(this)) {
            new DownloadMusicArtistsTask().execute();
        } else {
            Toast.makeText(MusicArtistsActivity.this, "You are offline. Please connect to the internet.",
                    Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    // Configure UI.

    private void configureUI() {
        activateToolbar();
        configureRecyclerView();
    }

    private void configureRecyclerView() {
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addItemDecoration(new DividerItemDecoration(this, R.drawable.divider));

        musicArtistRecyclerViewAdapter = new MusicArtistRecyclerViewAdapter(MusicArtistsActivity.this,
                artistList);
        recyclerView.setAdapter(musicArtistRecyclerViewAdapter);

        recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(this, recyclerView,
                new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        Intent detailIntent = new Intent(MusicArtistsActivity.this, ArtistDetailActivity.class);
                        detailIntent.putExtra(ARTIST_TRANSFER, artistList.get(position));
                        startActivity(detailIntent);
                    }
                }));
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

            Collections.sort(artists);
            setArtistList(artists);
            Log.d(LOG_TAG, "Fetched " + getArtistList().size() + " artists");

            musicArtistRecyclerViewAdapter.updateWithNewData(getArtistList());
        }

    }

}
