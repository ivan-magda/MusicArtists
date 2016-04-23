package com.ivanmagda.musicartists.ui.fragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.ivanmagda.musicartists.R;
import com.ivanmagda.musicartists.api.ArtistHttpApi;
import com.ivanmagda.musicartists.model.Artist;
import com.ivanmagda.musicartists.util.PersistenceUtils;
import com.ivanmagda.musicartists.Extras;
import com.ivanmagda.musicartists.ui.controllers.ArtistDetailActivity;
import com.ivanmagda.musicartists.ui.view.ArtistsListRecyclerViewAdapter;
import com.ivanmagda.musicartists.ui.view.DividerItemDecoration;
import com.ivanmagda.musicartists.ui.view.RecyclerItemClickListener;
import com.ivanmagda.musicartists.util.ConnectivityUtils;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.ivanmagda.musicartists.util.PersistenceUtils.ARTISTS_PERSISTENCE_KEY;

public class ArtistsListFragment extends Fragment {

    // Properties.

    private static final String LOG_TAG = ArtistsListFragment.class.getSimpleName();

    private SwipeRefreshLayout swipeRefreshLayout;

    private ArtistsListRecyclerViewAdapter artistsListRecyclerViewAdapter;
    private List<Artist> artistsList = new ArrayList<>();

    // Initializes.

    public static ArtistsListFragment newInstance() {
        return new ArtistsListFragment();
    }

    public ArtistsListFragment() {
        // Required empty public constructor.
    }

    // Life Cycle.

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getArtists();
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

        recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(activity, recyclerView,
                new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        onArtistSelected(artistsList.get(position));
                    }
                }));

        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipeRefreshLayout);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                downloadArtists();
            }
        });

        if (artistsList != null) {
            artistsListRecyclerViewAdapter.updateWithNewData(artistsList);
        }

        return view;
    }

    // Helpers.

    void onArtistSelected(Artist artist) {
        Intent detailIntent = new Intent(getActivity(), ArtistDetailActivity.class);
        detailIntent.putExtra(Extras.EXTRA_ARTIST_TRANSFER, (Parcelable) artist);
        startActivity(detailIntent);

        Log.d(LOG_TAG, "Did select " + artist.getName());
    }

    // Persistence storage support.

    private void getArtists() {
        Context context = getActivity();

        try {
            artistsList = (List<Artist>) PersistenceUtils.readObject(context,
                    ARTISTS_PERSISTENCE_KEY);
            Log.d(LOG_TAG, "Successfully retrieved artists from the persistence storage");
        } catch (FileNotFoundException e) {
            Log.e(LOG_TAG, "Currently there is no data. Need to download first", e);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Failed to read artists list from persistence", e);
        } catch (ClassNotFoundException e) {
            Log.e(LOG_TAG, "Failed to read artists list with ClassNotFoundError", e);
        }

        if (artistsList == null || artistsList.size() == 0) {
            downloadArtists();
        }
    }

    private void downloadArtists() {
        Context context = getActivity();
        if (ConnectivityUtils.isOnline(context)) {
            new DownloadMusicArtistsTask().execute();
        } else {
            swipeRefreshLayout.setRefreshing(false);
            Toast.makeText(context, "You are offline. Please connect to the internet.",
                    Toast.LENGTH_SHORT).show();
        }
    }

    // Save the list of artists to persistence storage.
    private void saveArtists(List<Artist> artistsList) {
        try {
            PersistenceUtils.writeObject(getActivity(), ARTISTS_PERSISTENCE_KEY,
                    artistsList);
            Log.d(LOG_TAG, "Successfully saved artists to the persistence storage");
        } catch (IOException exception) {
            Log.e(LOG_TAG, "Failed to write artists list to persistence storage", exception);
        }
    }

    // DownloadMusicArtistsTask.

    private class DownloadMusicArtistsTask extends AsyncTask<String, Void, List<Artist>> {
        @Override
        protected List<Artist> doInBackground(String... params) {
            Log.d(LOG_TAG, "Downloading artists...");
            return ArtistHttpApi.getArtists();
        }

        @Override
        protected void onPostExecute(List<Artist> artists) {
            super.onPostExecute(artists);

            if (artists == null) {
                return;
            }

            Collections.sort(artists);
            artistsList = artists;
            artistsListRecyclerViewAdapter.updateWithNewData(artistsList);
            saveArtists(artistsList);

            Log.d(LOG_TAG, "Fetched " + artistsList.size() + " artists");

            swipeRefreshLayout.setRefreshing(false);
        }

    }

}
