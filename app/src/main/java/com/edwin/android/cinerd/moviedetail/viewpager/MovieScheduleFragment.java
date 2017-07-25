package com.edwin.android.cinerd.moviedetail.viewpager;


import android.app.Fragment;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.edwin.android.cinerd.R;
import com.edwin.android.cinerd.data.CineRdContract;
import com.edwin.android.cinerd.entity.db.MovieTheaterDetail;
import com.edwin.android.cinerd.entity.json.Movie;
import com.edwin.android.cinerd.entity.StringSearcheable;
import com.edwin.android.cinerd.entity.json.Theater;
import com.edwin.android.cinerd.util.DateUtil;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import ir.mirrajabi.searchdialog.SimpleSearchDialogCompat;
import ir.mirrajabi.searchdialog.core.BaseSearchDialogCompat;
import ir.mirrajabi.searchdialog.core.SearchResultListener;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MovieScheduleFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MovieScheduleFragment extends Fragment implements MovieScheduleAdapter
        .ScheduleDayClicked {


    public static final Date todayDate = new Date();
    public static final String TAG = MovieScheduleFragment.class.getSimpleName();
    public static final String ARGUMENT_MOVIE = "MOVIE";
    public static final String ARGUMENT_HOST_FRAGMENT_TAG = "ARGUMENT_HOST_FRAGMENT_TAG";
    Unbinder unbinder;
    @BindView(R.id.recycler_view_movie_schedule)
    RecyclerView mRecyclerView;
    @BindView(R.id.text_theater_name)
    TextView textTheaterName;
    @BindView(R.id.recycler_view_movie_time)
    RecyclerView mMovieTimeRecyclerView;
    private MovieScheduleAdapter mAdapter;
    private Movie mMovie;
    private MovieTimeFormatAdapter mMovieTimeFormatAdapter;

    public MovieScheduleFragment() {
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param movie
     * @return a new instance of fragment MovieScheduleFragment
     */
    public static MovieScheduleFragment newInstance(Movie movie, String fragmentHostTag) {
        MovieScheduleFragment fragment = new MovieScheduleFragment();
        Bundle args = new Bundle();
        args.putParcelable(ARGUMENT_MOVIE, movie);
        Log.d(TAG, "fragmentHostTag: " + fragmentHostTag);
        args.putString(ARGUMENT_HOST_FRAGMENT_TAG, fragmentHostTag);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mMovie = getArguments().getParcelable(ARGUMENT_MOVIE);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_movie_schedule, container, false);
        unbinder = ButterKnife.bind(this, view);
        mAdapter = new MovieScheduleAdapter(this);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 3);


        mRecyclerView.setLayoutManager(gridLayoutManager);
        mRecyclerView.setHasFixedSize(false);
        mRecyclerView.setAdapter(mAdapter);
        List<Date> dates = getDates();
        mAdapter.setDates(dates);



        mMovieTimeFormatAdapter = new MovieTimeFormatAdapter();

        LinearLayoutManager linearLayoutManager
                = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        mMovieTimeRecyclerView.setLayoutManager(linearLayoutManager);
        mMovieTimeRecyclerView.setHasFixedSize(false);
        mMovieTimeRecyclerView.setNestedScrollingEnabled(true);
        mMovieTimeRecyclerView.setAdapter(mMovieTimeFormatAdapter);

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }


    private List<Date> getDates() {
        List<Date> dates = new ArrayList<>();
        dates.add(todayDate);
        dates.add(DateUtil.addDay(todayDate, 1));
        dates.add(DateUtil.addDay(todayDate, 2));
        return dates;
    }

    @Override
    public void onClickDay(Date date) {
        Log.d(TAG, "Date clicked: " + date);
        Toast.makeText(getActivity(), "Touched", Toast.LENGTH_SHORT).show();
    }

    @OnClick(R.id.text_theater_name)
    public void onViewClicked() {
        Log.d(TAG, "Theater name clicked");

        Log.d(TAG, "Movie data: "+ mMovie);

        String dialogTitle = MovieScheduleFragment.this.getString(R.string
                .movie_theater_search_dialog_title);
        String inputPlaceHolder = MovieScheduleFragment.this.getString(R.string
                .movie_theater_search_dialog_input_place_holder);

        long movieId = getMovieIdByName(mMovie.getName().toUpperCase());
        Log.d(TAG, "MovieID: "+ movieId);

        List<MovieTheaterDetail> movieTheaterDetails =
                getMoviesTheaterDetailByMovieIdAvailableDate(movieId, new Date());

        Log.d(TAG, "movieTheaterDetails: " + movieTheaterDetails);

        Set<StringSearcheable> theatersName = new HashSet<>();
        for(MovieTheaterDetail detail : movieTheaterDetails) {
            String theaterName = getTheaterNameById(detail.getTheaterId());
            theatersName.add(new StringSearcheable(theaterName));
        }


        new SimpleSearchDialogCompat(getActivity(), dialogTitle,
                inputPlaceHolder, null, new ArrayList<StringSearcheable>(theatersName),
                new SearchResultListener<Theater>() {
                    @Override
                    public void onSelected(BaseSearchDialogCompat dialog, Theater
                            theater, int position) {
                        Toast.makeText(MovieScheduleFragment.this.getActivity(), theater.getTitle(),
                                Toast.LENGTH_SHORT).show();

                        MovieScheduleFragment.this.textTheaterName.setText(theater.getTitle());

                        //MovieScheduleFragment.this.setMovieTheaterDetail();
                        dialog.dismiss();

                    }
                }).show();
    }


    private List<MovieTheaterDetail> getMoviesTheaterDetailByMovieIdAvailableDate(long movieId, Date availableDate) {
        List<MovieTheaterDetail> movieTheaterDetailList = new ArrayList<>();
        Cursor movieTheaterDetailCursor = null;
        try {
            movieTheaterDetailCursor = getActivity().getContentResolver().query(CineRdContract
                    .MovieTheaterDetailEntry
                    .CONTENT_URI, null, CineRdContract.MovieTheaterDetailEntry
                    .COLUMN_NAME_MOVIE_ID + " = ? AND date(" + CineRdContract
                    .MovieTheaterDetailEntry
                    .COLUMN_NAME_AVAILABLE_DATE + ") < date('" + DateUtil.formatDate
                    (availableDate) + "')", new String[]{String.valueOf(movieId)}, null);

            MovieTheaterDetail movieTheaterDetail;
            while (movieTheaterDetailCursor.moveToNext()) {
                Log.d(TAG, "movieTheaterDetailCursor.getCount(): " + movieTheaterDetailCursor.getCount());
                short roomId = movieTheaterDetailCursor.getShort(movieTheaterDetailCursor
                        .getColumnIndexOrThrow(CineRdContract.MovieTheaterDetailEntry
                                .COLUMN_NAME_ROOM_ID));
                short subtitleId = movieTheaterDetailCursor.getShort(movieTheaterDetailCursor
                        .getColumnIndex(CineRdContract.MovieTheaterDetailEntry
                                .COLUMN_NAME_SUBTITLE_ID));
                short formatId = movieTheaterDetailCursor.getShort(movieTheaterDetailCursor
                        .getColumnIndex(CineRdContract.MovieTheaterDetailEntry
                                .COLUMN_NAME_FORMAT_ID));
                short languageId = movieTheaterDetailCursor.getShort(movieTheaterDetailCursor
                        .getColumnIndex(CineRdContract.MovieTheaterDetailEntry
                                .COLUMN_NAME_LANGUAGE_ID));
                int theaterId = movieTheaterDetailCursor.getInt(movieTheaterDetailCursor
                        .getColumnIndex(CineRdContract.MovieTheaterDetailEntry
                                .COLUMN_NAME_THEATER_ID));

                movieTheaterDetail = new MovieTheaterDetail();
                movieTheaterDetail.setRoomId(roomId);
                movieTheaterDetail.setSubtitleId(subtitleId);
                movieTheaterDetail.setFormatId(formatId);
                movieTheaterDetail.setLanguageId(languageId);
                movieTheaterDetail.setTheaterId(theaterId);
                movieTheaterDetail.setMovieId(movieId);

                movieTheaterDetailList.add(movieTheaterDetail);
            }
            movieTheaterDetailCursor.close();

            return movieTheaterDetailList;
        } finally {
            if(movieTheaterDetailCursor != null) {
                movieTheaterDetailCursor.close();
            }
        }
    }

    private String getTheaterNameById(int theaterId) {
        Cursor theaterCursor = null;

        try {
            theaterCursor = getActivity().getContentResolver().query(CineRdContract.TheaterEntry
                            .CONTENT_URI, null,
                    CineRdContract.TheaterEntry._ID + " = ?", new String[]{String.valueOf(theaterId)},

                    null);

            if (theaterCursor.moveToNext()) {
                String theaterName = theaterCursor.getString(theaterCursor.getColumnIndex(CineRdContract.TheaterEntry.COLUMN_NAME_NAME));
                Log.d(TAG, "theaterName: " + theaterName);
                return theaterName;
            }
            return null;
        } finally {
            if(theaterCursor != null) {
                theaterCursor.close();
            }
        }


    }

    private String getFormatNameById(int formatId) {
        String formatName = "";
        Cursor formatCursor = null;
        try {
            formatCursor = getActivity().getContentResolver().query(CineRdContract.FormatEntry

                    .CONTENT_URI, null, CineRdContract.FormatEntry._ID + "=?", new
                    String[]{String.valueOf(formatId)}, null);
            if (formatCursor.moveToNext()) {
                formatName = formatCursor.getString(formatCursor.getColumnIndex(CineRdContract.FormatEntry.COLUMN_NAME_NAME));
                Log.d(TAG, "format name: " + formatName);
            }
            return formatName;
        } finally {
            if(formatCursor != null) {
                formatCursor.close();
            }
        }
    }

    private long getMovieIdByName(String movieName) {
        long movieId = -1;
        Cursor movieCursor = null;
        try {
            movieCursor = getActivity().getContentResolver().query(CineRdContract.MovieEntry
                    .CONTENT_URI, new String[]{CineRdContract.MovieEntry._ID}, "UPPER(NAME) = ?", new String[]{movieName}, null);

            if (movieCursor.moveToNext()) {
                movieId = movieCursor.getLong(movieCursor.getColumnIndex(CineRdContract.MovieEntry._ID));
            }

            return movieId;
        }finally {
            if(movieCursor != null) {
                movieCursor.close();
            }
        }
    }

    public void setMovieTheaterDetail(String movieId, String theaterName) {
        //Log.d(TAG, "Rooms: "+ rooms);
        //mMovieTimeFormatAdapter.setRooms(rooms);
    }

}
