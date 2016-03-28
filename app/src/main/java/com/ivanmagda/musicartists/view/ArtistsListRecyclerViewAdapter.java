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

public class ArtistsListRecyclerViewAdapter extends RecyclerView.Adapter<ArtistsViewHolder> {

    // Properties.

    private List<Artist> artistList;
    private Context context;

    // Initialize.

    public ArtistsListRecyclerViewAdapter(Context context, List<Artist> artistList) {
        this.context = context;
        this.artistList = artistList;
    }

    // Methods.

    @Override
    public ArtistsViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.artist_item, null);
        return new ArtistsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ArtistsViewHolder artistViewHolder, int position) {
        Artist artist = artistList.get(position);

        Picasso.with(context)
                .load(artist.getCover().getSmall())
                .error(R.drawable.placeholder)
                .placeholder(R.drawable.placeholder)
                .into(artistViewHolder.coverImageView);

        artistViewHolder.nameTextView.setText(artist.getName());
        artistViewHolder.genresTextView.setText(artist.getGenresString());

        String artistSummary = artist.getAlbumsSummary(context) + ", " + artist.getTracksSummary(context);
        artistViewHolder.summaryTextView.setText(artistSummary);
    }

    @Override
    public int getItemCount() {
        return (artistList == null ? 0 : artistList.size());
    }

    // Helpers.

    public void updateWithNewData(List<Artist> newArtists) {
        artistList = newArtists;
        notifyDataSetChanged();;
    }

}
