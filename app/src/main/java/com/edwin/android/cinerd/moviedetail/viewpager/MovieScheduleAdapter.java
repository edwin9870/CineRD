package com.edwin.android.cinerd.moviedetail.viewpager;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.edwin.android.cinerd.R;
import com.edwin.android.cinerd.util.DateUtil;
import com.edwin.android.cinerd.util.ResourceUtil;

import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Edwin Ramirez Ventura on 7/21/2017.
 */

public class MovieScheduleAdapter extends RecyclerView.Adapter<MovieScheduleAdapter.MoviePosterAdapterViewHolder> {

    public static final String TAG = MovieScheduleAdapter.class.getSimpleName();
    private final Context mContext;
    private List<Date> mDates;
    private ScheduleDayClicked scheduleDayClicked;
    private Date mSelectedDate;

    public MovieScheduleAdapter(Context context, ScheduleDayClicked scheduleDayClicked) {
        this.scheduleDayClicked = scheduleDayClicked;
        mContext = context;
    }

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

        Log.d(TAG, "mSelectedDate: " + mSelectedDate + ", date: " + date);
        if(mSelectedDate != null && DateUtil.areSameDay(date, mSelectedDate)) {
            Log.d(TAG, "Setting holder TextView color");
            holder.mScheduleDayNameTextView.setTextColor(ResourceUtil.getResourceColor(mContext, R.color.colorAccent));
            holder.mScheduleDayTextView.setTextColor(ResourceUtil.getResourceColor(mContext, R.color.colorAccent));
        }

        Log.d(TAG, "Setup schedule day and name");
    }

    @Override
    public int getItemCount() {
        if(mDates == null) {
            return 0;
        }
        return mDates.size();
    }

    class MoviePosterAdapterViewHolder extends RecyclerView.ViewHolder implements View
            .OnClickListener {
        @BindView(R.id.text_schedule_day_name)
        TextView mScheduleDayNameTextView;
        @BindView(R.id.text_schedule_day)
        TextView mScheduleDayTextView;

        MoviePosterAdapterViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            scheduleDayClicked.onClickDay(view, mDates.get(getAdapterPosition()));
        }
    }

    public void setDates(List<Date> dates, Date selectedDate) {
        this.mDates = dates;
        mSelectedDate = selectedDate;
        notifyDataSetChanged();
    }

    public interface ScheduleDayClicked {
        void onClickDay(View view, Date date);
    }
}
