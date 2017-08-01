package com.edwin.android.cinerd.movieposter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.edwin.android.cinerd.R;
import com.edwin.android.cinerd.entity.Movie;
import com.squareup.picasso.Picasso;

import java.util.List;
import java.util.logging.Logger;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Edwin Ramirez Ventura on 7/12/2017.
 */

public class MoviePosterAdapter extends RecyclerView.Adapter<MoviePosterAdapter
        .MoviePosterAdapterViewHolder> {

    public static final String TAG = MoviePosterAdapter.class.toString();
    private List<Movie> mMovies;
    private Context mContext;
    private MoviePosterListener mMoviePosterListener;

    public MoviePosterAdapter(Context context, MoviePosterListener moviePosterListener) {
        this.mContext = context;
        this.mMoviePosterListener = moviePosterListener;
    }

    @Override
    public MoviePosterAdapterViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        Context context = viewGroup.getContext();
        int idLayoutForMovieItem = R.layout.item_movie_list;
        LayoutInflater inflater = LayoutInflater.from(context);

        View view = inflater.inflate(idLayoutForMovieItem, viewGroup, false);
        return new MoviePosterAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MoviePosterAdapterViewHolder holder, int position) {
        String imageURL = null;
        try {
            Picasso picasso = Picasso.with(mContext);
            picasso.load(R.drawable.maxmaxposter).fit()
                    .into(holder.mMoviePosterImageView);
            Movie movie = mMovies.get(position);
            holder.mMovieNameTextView.setText(movie.getName());
            Log.d(TAG, "Rating movie: "+ mMovies.get(position).getRating());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        if(null == mMovies) {
            return 0;
        }

        Log.d(TAG, "Movies size:" + mMovies.size());
        return mMovies.size();
    }

    class MoviePosterAdapterViewHolder extends RecyclerView.ViewHolder implements View
            .OnClickListener {

        @BindView(R.id.image_movie_poster)
        ImageView mMoviePosterImageView;
        @BindView(R.id.text_movie_name)
        TextView mMovieNameTextView;

        MoviePosterAdapterViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int adapterPosition = getAdapterPosition();
            mMoviePosterListener.onClickMovie(MoviePosterAdapter.this.mMovies.get(adapterPosition));
        }
    }

    public void setMovies(List<Movie> movies) {
        this.mMovies = movies;
        notifyDataSetChanged();
    }

    public interface MoviePosterListener {
        void onClickMovie(Movie movie);
    }
}
