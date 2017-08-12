package com.edwin.android.cinerd.moviedetail.viewpager;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.edwin.android.cinerd.R;
import com.edwin.android.cinerd.entity.json.Room;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.TimeZone;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Edwin Ramirez Ventura on 7/22/2017.
 */

public class MovieTimeFormatAdapter extends RecyclerView.Adapter<MovieTimeFormatAdapter.MovieTimeFormatAdapterViewHolder>  {



    public static final String TAG = MovieTimeFormatAdapter.class.getSimpleName();
    public static final String NO_FORMAT = "N/A";
    private List<Room> mRooms;
    private Context mContext;

    public MovieTimeFormatAdapter(Context context) {
        this.mContext = context;
    }

    @Override
    public MovieTimeFormatAdapterViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        Context context = viewGroup.getContext();
        int idItemMovieSchedule = R.layout.item_movie_time_format;
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(idItemMovieSchedule, viewGroup, false);
        return new MovieTimeFormatAdapter.MovieTimeFormatAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MovieTimeFormatAdapterViewHolder holder, int position) {
        Room room = mRooms.get(position);

        java.text.DateFormat df = new SimpleDateFormat(mContext.getString(R.string.date_hour_minute)); // Quoted "Z" to indicate
        Log.d(TAG, "room.getDate(): " + room.getDate());
        CharSequence hourMinute = df.format(room.getDate());

        holder.mMovieTimeTextView.setText(hourMinute);
        if(!room.getFormat().toUpperCase().equals(NO_FORMAT)) {
            holder.mMovieFormatTextView.setText(room.getFormat().toUpperCase());
        }
    }

    @Override
    public int getItemCount() {
        if(mRooms == null) {
            return 0;
        }
        return mRooms.size();
    }

    class MovieTimeFormatAdapterViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.text_movie_time)
        TextView mMovieTimeTextView;
        @BindView(R.id.text_movie_format)
        TextView mMovieFormatTextView;

        MovieTimeFormatAdapterViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

    public void setRooms(List<Room> rooms) {
        this.mRooms = rooms;
        notifyDataSetChanged();
    }
}
