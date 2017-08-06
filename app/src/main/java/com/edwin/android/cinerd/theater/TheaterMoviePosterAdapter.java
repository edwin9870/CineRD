package com.edwin.android.cinerd.theater;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.edwin.android.cinerd.R;
import com.edwin.android.cinerd.entity.Movie;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Edwin Ramirez Ventura on 7/31/2017.
 */

public class TheaterMoviePosterAdapter extends RecyclerView.Adapter<TheaterMoviePosterAdapter
        .TheaterMoviePosterAdapterViewHolder> {

    private List<Movie> mMovies;
    private Context mContext;
    private TheaterMoviePosterAdapter.TheaterMoviePosterListener mMoviePosterListener;

    @Override
    public TheaterMoviePosterAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(TheaterMoviePosterAdapterViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }


    class TheaterMoviePosterAdapterViewHolder extends RecyclerView.ViewHolder implements View
            .OnClickListener {

        @BindView(R.id.image_movie_poster)
        ImageView mMoviePosterImageView;
        @BindView(R.id.text_movie_name)
        TextView mMovieNameTextView;

        TheaterMoviePosterAdapterViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int adapterPosition = getAdapterPosition();
        }
    }

    public void setMovies(List<Movie> movies) {
        this.mMovies = movies;
        notifyDataSetChanged();
    }

    interface TheaterMoviePosterListener {
        void onClickMovie(Movie movie);
    }
}
