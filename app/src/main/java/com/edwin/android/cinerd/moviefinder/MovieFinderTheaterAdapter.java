package com.edwin.android.cinerd.moviefinder;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.edwin.android.cinerd.R;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Edwin Ramirez Ventura on 7/30/2017.
 */

public class MovieFinderTheaterAdapter extends RecyclerView.Adapter<MovieFinderTheaterAdapter
        .MovieFinderTheaterAdapterViewHolder> {
    public static final String TAG = MovieFinderTheaterAdapter.class.getSimpleName();
    private List<String> mTheatersName;

    @Override
    public MovieFinderTheaterAdapterViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        Context context = viewGroup.getContext();
        int idLayoutForMovieItem = R.layout.item_movie_theater;
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(idLayoutForMovieItem, viewGroup, false);
        return new MovieFinderTheaterAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MovieFinderTheaterAdapterViewHolder holder, int position) {
        holder.mMovieFinderTheaterName.setText(mTheatersName.get(position));
    }

    @Override
    public int getItemCount() {
        if (null == mTheatersName) {
            return 0;
        }

        Log.d(TAG, "mTheatersName size:" + mTheatersName.size());
        return mTheatersName.size();
    }

    class MovieFinderTheaterAdapterViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.text_movie_finder_theater_name)
        TextView mMovieFinderTheaterName;

        MovieFinderTheaterAdapterViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public void setTheatersName(List<String> theatersName) {
        this.mTheatersName = theatersName;
        notifyDataSetChanged();
    }
}
