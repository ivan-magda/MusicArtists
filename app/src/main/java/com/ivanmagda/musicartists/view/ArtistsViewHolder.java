package com.ivanmagda.musicartists.view;


import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.ivanmagda.musicartists.R;

public class ArtistsViewHolder extends RecyclerView.ViewHolder {

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
