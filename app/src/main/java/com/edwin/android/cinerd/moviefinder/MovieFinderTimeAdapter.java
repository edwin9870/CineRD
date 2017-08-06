package com.edwin.android.cinerd.moviefinder;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.edwin.android.cinerd.R;
import com.edwin.android.cinerd.entity.db.MovieTheaterDetail;

import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Edwin Ramirez Ventura on 7/29/2017.
 */

public class MovieFinderTimeAdapter extends RecyclerView.Adapter<MovieFinderTimeAdapter
        .MovieFinderTimeViewHolder> {


    public static final String TAG = MovieFinderTimeAdapter.class.getSimpleName();
    private List<MovieTheaterDetail> mMovieTheaterDetails;
    private MovieFinderTimeLister mListener;
    private Context mContext;

    public MovieFinderTimeAdapter(MovieFinderTimeLister lister, Context context) {
        this.mListener = lister;
        mContext = context;
    }

    @Override
    public MovieFinderTimeViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        Context context = viewGroup.getContext();
        int idLayoutForMovieItem = R.layout.item_movie_finder_time;
        LayoutInflater inflater = LayoutInflater.from(context);

        View view = inflater.inflate(idLayoutForMovieItem, viewGroup, false);
        return new MovieFinderTimeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MovieFinderTimeViewHolder holder, int position) {

        Date availableDate = mMovieTheaterDetails.get(position).getAvailableDate();
        CharSequence availableDateFormatted = DateFormat.format(mContext.getString(R.string.date_hour_minute),
                availableDate);
        holder.mMovieFinderTimeTextView.setText(availableDateFormatted);
    }

    @Override
    public int getItemCount() {
        if (null == mMovieTheaterDetails) {
            return 0;
        }

        Log.d(TAG, "mMovieTheaterDetails size:" + mMovieTheaterDetails.size());
        return mMovieTheaterDetails.size();
    }

    class MovieFinderTimeViewHolder extends RecyclerView.ViewHolder implements View
            .OnClickListener {

        @BindView(R.id.text_movie_finder_time)
        TextView mMovieFinderTimeTextView;

        MovieFinderTimeViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int adapterPosition = getAdapterPosition();
            Log.d(TAG, "view clicked");
            mListener.onClick(mMovieTheaterDetails.get(adapterPosition));
        }
    }

    public void setMovieTheaterDetails(List<MovieTheaterDetail> movieTheaterDetails) {
        this.mMovieTheaterDetails = movieTheaterDetails;
        notifyDataSetChanged();
    }

    interface MovieFinderTimeLister {
        void onClick(MovieTheaterDetail movieTheaterDetail);
    }
}
