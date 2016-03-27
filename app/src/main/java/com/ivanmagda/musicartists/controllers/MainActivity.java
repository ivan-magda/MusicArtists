package com.ivanmagda.musicartists.controllers;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.ivanmagda.musicartists.view.DividerItemDecoration;
import com.ivanmagda.musicartists.view.MusicArtistRecyclerViewAdapter;
import com.ivanmagda.musicartists.R;
import com.ivanmagda.musicartists.api.ArtistHttpApi;
import com.ivanmagda.musicartists.model.Artist;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    // Properties.

    private static final String LOG_TAG = MainActivity.class.getSimpleName();

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
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addItemDecoration(new DividerItemDecoration(this, R.drawable.divider));

        // Check the Network Connection.
        if (ArtistHttpApi.isOnline(this)) {
            new DownloadMusicArtistsTask().execute();
        } else {
            Toast.makeText(MainActivity.this, "You are offline. Please connect to the internet.",
                    Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
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

            musicArtistRecyclerViewAdapter = new MusicArtistRecyclerViewAdapter(MainActivity.this,
                    getArtistList());
            recyclerView.setAdapter(musicArtistRecyclerViewAdapter);
        }

    }

}
