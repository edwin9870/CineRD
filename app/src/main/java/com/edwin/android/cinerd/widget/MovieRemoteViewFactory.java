package com.edwin.android.cinerd.widget;

import android.content.Context;
import android.os.Binder;
import android.text.TextUtils;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.edwin.android.cinerd.R;
import com.edwin.android.cinerd.configuration.di.ApplicationModule;
import com.edwin.android.cinerd.configuration.di.DaggerDatabaseComponent;
import com.edwin.android.cinerd.data.repositories.GenreRepository;
import com.edwin.android.cinerd.data.repositories.MovieRepository;
import com.edwin.android.cinerd.entity.Genre;
import com.edwin.android.cinerd.entity.Movie;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Edwin Ramirez Ventura on 5/27/2017.
 */

public class MovieRemoteViewFactory implements RemoteViewsService.RemoteViewsFactory {

    public static final String TAG = MovieRemoteViewFactory.class.getSimpleName();
    private Context mContext;
    private List<Movie> movies;
    MovieRepository mMovieRepository;
    GenreRepository mGenreRepository;

    public MovieRemoteViewFactory(Context applicationContext) {
        mContext = applicationContext;
        mMovieRepository = DaggerDatabaseComponent.builder()
                .applicationModule
                        (new ApplicationModule(applicationContext)).build().getMovieRepository();
        mGenreRepository = DaggerDatabaseComponent.builder()
                .applicationModule
                        (new ApplicationModule(applicationContext)).build().getGenreRepository();
    }

    @Override
    public void onCreate() {}

    @Override
    public void onDataSetChanged() {
        Log.d(TAG, "Getting data");
        final long token = Binder.clearCallingIdentity();
        try {
            movies = mMovieRepository.getMovies();
        } finally {
            Binder.restoreCallingIdentity(token);
        }
        Log.d(TAG, "Movies size: "+ movies.size());
    }

    @Override
    public void onDestroy() {
    }

    @Override
    public int getCount() {
        Log.d(TAG, "Count called");
        if(movies == null) {
            return 0;
        }

        Log.d(TAG, "mRecipes size: "+ movies.size());
        return movies.size();
    }

    @Override
    public RemoteViews getViewAt(int position) {
        Log.d(TAG, "getViewAt: "+ position);
        RemoteViews views = new RemoteViews(mContext.getPackageName(), R.layout.item_movie_detail_widget);

        Movie movie = movies.get(position);
        views.setTextViewText(R.id.text_widget_movie_name, movie.getName());

        final long token = Binder.clearCallingIdentity();
        try {
            List<Genre> genres = mGenreRepository.getGenresByMovieId(movie.getMovieId());
            List<String> genresName = new ArrayList<>();
            for (Genre genre : genres) {
                genresName.add(genre.getName());
            }
            StringBuilder genreDuration = new StringBuilder(TextUtils.join(", ", genresName));
            genreDuration.append(" | ");
            genreDuration.append(String.valueOf(movie.getDuration()));
            genreDuration.append(" min");
            views.setTextViewText(R.id.text_movie_genre_duration, genreDuration);

            if (movie.getRating() != null &&
                    movie.getRating().getImdb() != null &&
                    movie.getRating()
                            .getImdb().length() > 0) {
                views.setTextViewText(R.id.text_imdb_value, movie.getRating().getImdb());
            }
            if (movie.getRating() != null &&
                    movie.getRating().getRottenTomatoes() != null &&
                    movie.getRating().getRottenTomatoes().length() > 0) {
                views.setTextViewText(R.id.text_rotten_tomatoes_value, movie.getRating().getRottenTomatoes());


            }
            return views;
        } finally {
            Binder.restoreCallingIdentity(token);
        }
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

}
