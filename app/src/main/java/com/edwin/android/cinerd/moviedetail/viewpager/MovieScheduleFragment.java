package com.edwin.android.cinerd.moviedetail.viewpager;


import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.edwin.android.cinerd.R;
import com.edwin.android.cinerd.data.repositories.FormatRepository;
import com.edwin.android.cinerd.data.repositories.MovieTheaterDetailRepository;
import com.edwin.android.cinerd.data.repositories.TheaterRepository;
import com.edwin.android.cinerd.entity.Theater;
import com.edwin.android.cinerd.entity.db.MovieTheaterDetail;
import com.edwin.android.cinerd.entity.json.Room;
import com.edwin.android.cinerd.util.DateUtil;
import com.edwin.android.cinerd.util.ResourceUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import butterknife.BindView;
import butterknife.ButterKnife;
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
    public static final String ARGUMENT_MOVIE_ID = "MOVIE";
    public static final String ARGUMENT_HOST_FRAGMENT_TAG = "ARGUMENT_HOST_FRAGMENT_TAG";
    public static final String BUNDLE_SELECTED_DATE = "BUNDLE_SELECTED_DATE";
    public static final String BUNDLE_SELECTED_THEATER_ID = "BUNDLE_SELECTED_THEATER_ID";
    public static final String BUNDLE_SELECTED_THEATER_TITLE = "BUNDLE_SELECTED_THEATER_TITLE";
    public static final String BUNDLE_RECYCLER_VIEW_ID = "BUNDLE_RECYCLER_VIEW_ID";

    Unbinder unbinder;
    @BindView(R.id.recycler_view_movie_schedule)
    RecyclerView mRecyclerView;
    @BindView(R.id.text_theater_name)
    TextView textTheaterName;
    @BindView(R.id.recycler_view_movie_time)
    RecyclerView mMovieTimeRecyclerView;
    @BindView(R.id.layout_movie_theater_info)
    LinearLayout movieTheaterInfoLinearLayout;
    private MovieScheduleAdapter mAdapter;
    private long mMovieId;
    private MovieTimeFormatAdapter mMovieTimeFormatAdapter;
    private TheaterRepository mTheaterRepository;
    private MovieTheaterDetailRepository mMovieTheaterDetailRepository;
    private FormatRepository formatRepository;
    private String mSelectedTheaterTitle;
    private int mSelectedTheaterId;
    private TextView mSelectedScheduleDayNameTextView;
    private TextView mSelectedScheduleDayTextView;
    private int mOriginalTextColor;
    private Date mSelectedDate;

    public MovieScheduleFragment() {
    }


    public static MovieScheduleFragment newInstance(long movieId, String fragmentHostTag) {
        MovieScheduleFragment fragment = new MovieScheduleFragment();
        Bundle args = new Bundle();
        args.putLong(ARGUMENT_MOVIE_ID, movieId);
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
            mMovieId = getArguments().getLong(ARGUMENT_MOVIE_ID);
        }
    }

    private void restorePreviousState(Bundle savedInstanceState) {
        if(savedInstanceState != null) {
            Log.d(TAG, "Getting values previously saved");
            long selectedDateLong = savedInstanceState.getLong(BUNDLE_SELECTED_DATE);
            mSelectedTheaterId = savedInstanceState.getInt(BUNDLE_SELECTED_THEATER_ID);
            mSelectedTheaterTitle = savedInstanceState.getString(BUNDLE_SELECTED_THEATER_TITLE);
            Log.d(TAG, "onViewStateRestored method. mSelectedDateLong: " + selectedDateLong);
            Log.d(TAG, "onViewStateRestored method. mSelectedTheaterId: " + mSelectedTheaterId);
            Log.d(TAG, "onViewStateRestored method. mSelectedTheaterTitle: " + mSelectedTheaterTitle);

            if(selectedDateLong > 0 && mSelectedTheaterId > 0 && mSelectedTheaterTitle != null) {
                Log.d(TAG, "Restoring view");
                Log.d(TAG, "mMovieId: "+ mMovieId);
                mSelectedDate = new Date(selectedDateLong);
                setMovieTheaterDetail(mMovieId, mSelectedTheaterId, mSelectedDate);
                textTheaterName.setText(mSelectedTheaterTitle);
                movieTheaterInfoLinearLayout.setVisibility(View.VISIBLE);
            }

            Parcelable recyclerViewState = savedInstanceState.getParcelable(BUNDLE_RECYCLER_VIEW_ID);

            if(recyclerViewState != null) {
                Log.d(TAG, "restoring RecyclerView state");
                mRecyclerView.getLayoutManager().onRestoreInstanceState(recyclerViewState);
            }

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_movie_schedule, container, false);
        unbinder = ButterKnife.bind(this, view);
        mAdapter = new MovieScheduleAdapter(getActivity(), this);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 3);


        mRecyclerView.setLayoutManager(gridLayoutManager);
        mRecyclerView.setHasFixedSize(false);
        mRecyclerView.setAdapter(mAdapter);

        mMovieTimeFormatAdapter = new MovieTimeFormatAdapter(getActivity());

        LinearLayoutManager linearLayoutManager
                = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        mMovieTimeRecyclerView.setLayoutManager(linearLayoutManager);
        mMovieTimeRecyclerView.setHasFixedSize(false);
        mMovieTimeRecyclerView.setNestedScrollingEnabled(true);
        mMovieTimeRecyclerView.setAdapter(mMovieTimeFormatAdapter);

        mMovieTheaterDetailRepository = new MovieTheaterDetailRepository(getActivity());
        formatRepository = new FormatRepository(getActivity());
        mTheaterRepository = new TheaterRepository(getActivity());


        restorePreviousState(savedInstanceState);

        List<Date> dates = getDates();
        mAdapter.setDates(dates, mSelectedDate);
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        Log.d(TAG, "onSaveInstanceState method. mSelectedDate: " + mSelectedDate);
        Log.d(TAG, "onSaveInstanceState method. mSelectedTheaterId: " + mSelectedTheaterId);
        Log.d(TAG, "onSaveInstanceState method. mSelectedTheaterTitle: " + mSelectedTheaterTitle);

        if(mSelectedDate != null) {
            outState.putLong(BUNDLE_SELECTED_DATE, mSelectedDate.getTime());
        }
        if(mSelectedTheaterId > 0) {

            outState.putInt(BUNDLE_SELECTED_THEATER_ID, mSelectedTheaterId);
        }
        if(mSelectedTheaterTitle != null) {
            outState.putString(BUNDLE_SELECTED_THEATER_TITLE, mSelectedTheaterTitle);
        }

        Parcelable mRecyclerViewState = mRecyclerView.getLayoutManager().onSaveInstanceState();
        outState.putParcelable(BUNDLE_RECYCLER_VIEW_ID, mRecyclerViewState);

        super.onSaveInstanceState(outState);
    }


    private List<Date> getDates() {
        List<Date> dates = new ArrayList<>();
        dates.add(todayDate);
        dates.add(DateUtil.addDay(todayDate, 1));
        dates.add(DateUtil.addDay(todayDate, 2));
        return dates;
    }

    @Override
    public void onClickDay(View view, Date date) {
        Log.d(TAG, "Date clicked: " + date);
        showMovieTheaterDetail(view, date);
    }

    public void showMovieTheaterDetail(final View dateViewClicked, final Date dateToShowMovies) {
        Log.d(TAG, "Theater name clicked");
        Log.d(TAG, "Movie data: " + mMovieId);


        String dialogTitle = MovieScheduleFragment.this.getString(R.string
                .movie_theater_search_dialog_title);
        String inputPlaceHolder = MovieScheduleFragment.this.getString(R.string
                .movie_theater_search_dialog_input_place_holder);


        Log.d(TAG, "MovieID: " + mMovieId);

        List<MovieTheaterDetail> movieTheaterDetails =
                mMovieTheaterDetailRepository.getMoviesTheaterDetailByMovieIdAvailableDate(mMovieId,
                        dateToShowMovies);

        Log.d(TAG, "movieTheaterDetails: " + movieTheaterDetails);

        Set<Theater> theatersName = new HashSet<>();
        for (MovieTheaterDetail detail : movieTheaterDetails) {
            String theaterName = mTheaterRepository.getTheaterById(detail.getTheaterId())
                    .getTitle();
            theatersName.add(new Theater(theaterName, detail.getTheaterId()));
        }


        ArrayList<Theater> theaters = new ArrayList<>(theatersName);
        Collections.sort(theaters);
        SimpleSearchDialogCompat searchDialogCompat = new SimpleSearchDialogCompat
                (getActivity(), dialogTitle,
                        inputPlaceHolder, null, theaters,
                        new SearchResultListener<Theater>() {
                            @Override
                            public void onSelected(BaseSearchDialogCompat dialog, Theater
                                    theaterSearchable, int position) {
                                Toast.makeText(MovieScheduleFragment.this.getActivity(),
                                        theaterSearchable.getTitle(),
                                        Toast.LENGTH_SHORT).show();

                                mSelectedTheaterId = theaterSearchable.getTheaterId();
                                mSelectedDate = dateToShowMovies;
                                MovieScheduleFragment.this.setMovieTheaterDetail(mMovieId,
                                        mSelectedTheaterId, mSelectedDate);
                                mSelectedTheaterTitle = theaterSearchable
                                        .getTitle();
                                MovieScheduleFragment.this.textTheaterName.setText(mSelectedTheaterTitle);
                                dialog.dismiss();
                                MovieScheduleFragment.this.movieTheaterInfoLinearLayout
                                        .setVisibility
                                                (View.VISIBLE);
                                changeScheduleSelectedDayColor(dateViewClicked);

                            }
                        });
        searchDialogCompat.getContext().setTheme(R.style.AppTheme_Dialog_Light_DarkText);
        searchDialogCompat.show();
    }

    private void changeScheduleSelectedDayColor(View dateViewClicked) {
        mOriginalTextColor = ((TextView) dateViewClicked.findViewById(R.id.text_schedule_day_name)).getCurrentTextColor();
        TextView currentScheduleDayNameTextView = dateViewClicked.findViewById(R.id.text_schedule_day_name);
        TextView currentScheduleDayTextView = dateViewClicked.findViewById(R.id.text_schedule_day);

        currentScheduleDayNameTextView.setTextColor(ResourceUtil.getResourceColor(getActivity(), R.color.color_accent));
        currentScheduleDayTextView.setTextColor(ResourceUtil.getResourceColor(getActivity(), R.color.color_accent));

        if(mSelectedScheduleDayTextView != null && mSelectedScheduleDayNameTextView != null) {
            mSelectedScheduleDayTextView.setTextColor(mOriginalTextColor);
            mSelectedScheduleDayNameTextView.setTextColor(mOriginalTextColor);
        }
        mSelectedScheduleDayNameTextView = dateViewClicked.findViewById(R.id.text_schedule_day_name);
        mSelectedScheduleDayTextView = dateViewClicked.findViewById(R.id.text_schedule_day);
    }

    public void setMovieTheaterDetail(long movieId, long theaterId, Date availableDate) {
        Log.d(TAG, "setMovieTheaterDetail.movieId: " + movieId);
        Log.d(TAG, "setMovieTheaterDetail.theaterId: " + theaterId);
        Log.d(TAG, "setMovieTheaterDetail.movieId: " + availableDate);
        List<MovieTheaterDetail> details =
                mMovieTheaterDetailRepository.getMoviesTheaterDetailByMovieIdAvailableDate(movieId,
                        availableDate, theaterId);
        Log.d(TAG, "details: " + details);
        Room room;
        List<Room> rooms = new ArrayList<>();
        for (MovieTheaterDetail detail : details) {
            room = new Room();
            room.setmDate(detail.getAvailableDate());
            room.setmFormat(formatRepository.getFormatNameById(detail.getFormatId()));
            rooms.add(room);
        }

        Collections.sort(rooms, new Comparator<Room>() {
            @Override
            public int compare(Room room, Room t1) {
                return room.getDate().compareTo(t1.getDate());
            }
        });
        Log.d(TAG, "rooms sored: "+rooms);
        mMovieTimeFormatAdapter.setRooms(rooms);
    }

}
