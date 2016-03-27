package com.ivanmagda.musicartists.view;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ivanmagda.musicartists.R;
import com.ivanmagda.musicartists.model.Artist;
import com.squareup.picasso.Picasso;

import java.util.List;

public class MusicArtistRecyclerViewAdapter extends RecyclerView.Adapter<MusicArtistViewHolder> {

    // Properties.

    private List<Artist> artistList;
    private Context context;

    // Initialize.

    public MusicArtistRecyclerViewAdapter(Context context, List<Artist> artistList) {
        this.context = context;
        this.artistList = artistList;
    }

    // Methods.

    @Override
    public MusicArtistViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.artist_item, null);
        return new MusicArtistViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MusicArtistViewHolder artistViewHolder, int position) {
        Artist artist = artistList.get(position);

        Picasso.with(context)
                .load(artist.getCover().getSmall())
                .error(R.drawable.placeholder)
                .placeholder(R.drawable.placeholder)
                .into(artistViewHolder.coverImageView);

        artistViewHolder.nameTextView.setText(artist.getName());
        artistViewHolder.genresTextView.setText(artist.getGenresString());

        // Set artist summary.
        String albumsString = getCorrectWord(artist.getAlbums(), "альбом", "альбома", "альбомов");
        String tracksString = getCorrectWord(artist.getTracks(), "песня", "песни", "песен");
        String artistSummary = artist.getAlbums() + " " + albumsString + ", "
                + artist.getTracks() + " " + tracksString;
        artistViewHolder.summaryTextView.setText(artistSummary);
    }

    @Override
    public int getItemCount() {
        return (artistList == null ? 0 : artistList.size());
    }

    // Helpers.

    /**
     * Proper completion of words or numbers and words, taking into Russian morphology.
     *
     * @param number  - amount value
     * @param single  - singular word string.
     * @param several - several word string.
     * @param many    - many word string.
     * @return - correct word based on Russian morphology.
     */
    private String getCorrectWord(int number, String single, String several, String many) {
        int value = number % 100;

        if (value > 10 && value < 20) {
            return many;
        } else {
            value = number % 10;

            if (value == 1) {
                return single;
            } else if (value > 1 && value < 5) {
                return several;
            } else {
                return many;
            }
        }
    }

}
