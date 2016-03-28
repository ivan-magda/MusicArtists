package com.ivanmagda.musicartists.fragments;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.ivanmagda.musicartists.R;
import com.ivanmagda.musicartists.api.ArtistHttpApi;
import com.ivanmagda.musicartists.controllers.ArtistsListActivity;
import com.ivanmagda.musicartists.model.Artist;
import com.ivanmagda.musicartists.view.ArtistsListRecyclerViewAdapter;
import com.ivanmagda.musicartists.view.DividerItemDecoration;
import com.ivanmagda.musicartists.view.RecyclerItemClickListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ArtistsListFragment extends Fragment {

    // Types.

    public interface OnArtistSelected {
        void onArtistSelected(Artist artist);
    }

    // Properties.

    private static final String LOG_TAG = ArtistsListActivity.class.getSimpleName();

    private OnArtistSelected listener;
    private ArtistsListRecyclerViewAdapter artistsListRecyclerViewAdapter;
    private List<Artist> artistsList = new ArrayList<>();

    // Initializes.

    public static ArtistsListFragment newInstance() {
        return new ArtistsListFragment();
    }

    public ArtistsListFragment() {
        // Required empty public constructor.
    }

    // Getters/Setters.

    public List<Artist> getArtistsList() {
        return artistsList;
    }

    public void setArtistsList(List<Artist> artistsList) {
        this.artistsList = artistsList;
        artistsListRecyclerViewAdapter.updateWithNewData(artistsList);
    }

    // Life Cycle.

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if (context instanceof OnArtistSelected) {
            listener = (OnArtistSelected) context;
        } else {
            throw new ClassCastException(context.toString() + " must implement OnArtistSelected");
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_artists_list, container, false);

        final Activity activity = getActivity();
        final RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(activity));
        recyclerView.addItemDecoration(new DividerItemDecoration(activity, R.drawable.divider));

        artistsListRecyclerViewAdapter = new ArtistsListRecyclerViewAdapter(
                activity,
                artistsList);
        recyclerView.setAdapter(artistsListRecyclerViewAdapter);

        recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(activity, recyclerView, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                listener.onArtistSelected(artistsList.get(position));
            }
        }));

        // Download artists list.
        if (ArtistHttpApi.isOnline(activity)) {
            new DownloadMusicArtistsTask().execute();
        } else {
            Toast.makeText(activity, "You are offline. Please connect to the internet.",
                    Toast.LENGTH_SHORT).show();
        }

        return view;
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
            setArtistsList(artists);
            Log.d(LOG_TAG, "Fetched " + getArtistsList().size() + " artists");
        }

    }

}
