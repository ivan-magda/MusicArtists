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
import com.ivanmagda.musicartists.model.Artist;
import com.ivanmagda.musicartists.model.Persistence;
import com.ivanmagda.musicartists.view.ArtistsListRecyclerViewAdapter;
import com.ivanmagda.musicartists.view.DividerItemDecoration;
import com.ivanmagda.musicartists.view.RecyclerItemClickListener;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.ivanmagda.musicartists.model.Persistence.ARTISTS_PERSISTENCE_KEY;

public class ArtistsListFragment extends Fragment {

    // Types.

    public interface OnArtistSelected {
        void onArtistSelected(Artist artist);
    }

    // Properties.

    private static final String LOG_TAG = ArtistsListFragment.class.getSimpleName();

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

        getArtists(activity);

        return view;
    }

    // Persistence storage support.

    private void getArtists(Context context) {
        List<Artist> artistsList = null;
        try {
            artistsList = (List<Artist>) Persistence.readObject(context,
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
            // Download artists list.
            if (ArtistHttpApi.isOnline(context)) {
                new DownloadMusicArtistsTask().execute();
            } else {
                Toast.makeText(context, "You are offline. Please connect to the internet.",
                        Toast.LENGTH_SHORT).show();
            }
        } else {
            setArtistsList(artistsList);
        }
    }

    // Save the list of artists to persistence storage.
    private void saveArtists(List<Artist> artistsList, Context context) {
        try {
            Persistence.writeObject(getActivity(), ARTISTS_PERSISTENCE_KEY,
                    getArtistsList());
            Log.d(LOG_TAG, "Successfully saved artists to the persistence storage");
        } catch (IOException exception) {
            Log.e(LOG_TAG, "Failed to write artists list to persistnce storage", exception);
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

            Collections.sort(artists);
            setArtistsList(artists);
            Log.d(LOG_TAG, "Fetched " + getArtistsList().size() + " artists");

            saveArtists(getArtistsList(), getActivity());
        }

    }

}
