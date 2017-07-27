package com.edwin.android.cinerd.movieposter.dialog;

import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.edwin.android.cinerd.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Created by Edwin Ramirez Ventura on 7/27/2017.
 */

public class MovieTheaterDialogFragment extends DialogFragment {

    public static final String TAG = MovieTheaterDialogFragment.class.getSimpleName();
    @BindView(R.id.image_movie_filter)
    ImageView mMovieFilterImageView;
    @BindView(R.id.image_theater_filter)
    ImageView mTheaterFilterImageView;
    Unbinder unbinder;
    private MovieTheaterDialogListener mListener;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mListener = (MovieTheaterDialogListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implement NoticeDialogListener");
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_movie_theater_filter, null);
        builder.setView(view);
        unbinder = ButterKnife.bind(this, view);

        return builder.create();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick({R.id.image_movie_filter, R.id.image_theater_filter})
    public void onViewClicked(View view) {
        Log.d(TAG, "onViewClicked fired");
        switch (view.getId()) {
            case R.id.image_movie_filter:
                mListener.onClickMovie();
                break;
            case R.id.image_theater_filter:
                mListener.onClickTheater();
                break;
        }
    }

    public interface MovieTheaterDialogListener {
        void onClickMovie();
        void onClickTheater();
    }
}
