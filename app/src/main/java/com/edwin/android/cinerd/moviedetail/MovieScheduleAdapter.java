package com.edwin.android.cinerd.moviedetail;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.edwin.android.cinerd.R;

import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Edwin Ramirez Ventura on 7/21/2017.
 */

public class MovieScheduleAdapter extends RecyclerView.Adapter<MovieScheduleAdapter.MoviePosterAdapterViewHolder> {

    public static final String TAG = MovieScheduleAdapter.class.getSimpleName();
    List<Date> mDates;

    @Override
    public MoviePosterAdapterViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        Context context = viewGroup.getContext();
        int idItemMovieSchedule = R.layout.item_movie_schedule;
        LayoutInflater inflater = LayoutInflater.from(context);

        View view = inflater.inflate(idItemMovieSchedule, viewGroup, false);
        return new MoviePosterAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MoviePosterAdapterViewHolder holder, int position) {
        Date date = mDates.get(position);
        CharSequence dayName = DateFormat.format("E", date);
        CharSequence dayNumber = DateFormat.format("d", date);

        holder.mScheduleDayNameTextView.setText(dayName.toString().toUpperCase());
        holder.mScheduleDayTextView.setText(dayNumber);

        Log.d(TAG, "Setup schedule day and name");
    }

    @Override
    public int getItemCount() {
        if(mDates == null) {
            return 0;
        }
        return mDates.size();
    }

    class MoviePosterAdapterViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.text_schedule_day_name)
        TextView mScheduleDayNameTextView;
        @BindView(R.id.text_schedule_day)
        TextView mScheduleDayTextView;

        MoviePosterAdapterViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

    public void setDates(List<Date> dates) {
        this.mDates = dates;
        notifyDataSetChanged();
    }
}
