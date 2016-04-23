package com.ivanmagda.musicartists.ui.view;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ivanmagda.musicartists.R;
import com.ivanmagda.musicartists.model.Artist;
import com.ivanmagda.musicartists.util.ArtistUtils;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ArtistsListRecyclerViewAdapter extends RecyclerView.Adapter<ArtistsListRecyclerViewAdapter.ArtistsViewHolder> {

    // Properties.

    private List<Artist> artistList;

    // Initialize.

    public ArtistsListRecyclerViewAdapter(List<Artist> artistList) {
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
        Context context = artistViewHolder.itemView.getContext();

        Picasso.with(context)
                .load(artist.getCover().getSmall())
                .error(R.drawable.placeholder)
                .placeholder(R.drawable.placeholder)
                .into(artistViewHolder.coverImageView);

        artistViewHolder.nameTextView.setText(artist.getName());

        // Genres description of the artist.
        String genresDescription = ArtistUtils.buildGenresArtistDescription(artist);
        if (genresDescription == null) {
            genresDescription = context.getString(R.string.genres_undefined_title);
        }
        artistViewHolder.genresTextView.setText(genresDescription);

        String artistSummary = ArtistUtils.buildArtistAlbumsSummary(artist, context)
                + ", "
                + ArtistUtils.buildArtistTracksSummary(artist, context);
        artistViewHolder.summaryTextView.setText(artistSummary);
    }

    @Override
    public int getItemCount() {
        return (artistList == null ? 0 : artistList.size());
    }

    // Helpers.

    public void updateWithNewData(List<Artist> newArtists) {
        artistList = newArtists;
        notifyDataSetChanged();
    }

    // ArtistsViewHolder.

    public static class ArtistsViewHolder extends RecyclerView.ViewHolder {

        // Properties.

        protected ImageView coverImageView;
        protected TextView nameTextView;
        protected TextView genresTextView;
        protected TextView summaryTextView;

        // Initialize.

        public ArtistsViewHolder(View view) {
            super(view);

            coverImageView = (ImageView) view.findViewById(R.id.cover_image_view);
            nameTextView = (TextView) view.findViewById(R.id.name_text_view);
            genresTextView = (TextView) view.findViewById(R.id.genres_text_view);
            summaryTextView = (TextView) view.findViewById(R.id.summary_text_view);
        }

    }

}
