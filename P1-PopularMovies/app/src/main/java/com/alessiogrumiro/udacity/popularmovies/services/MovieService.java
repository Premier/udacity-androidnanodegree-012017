package com.alessiogrumiro.udacity.popularmovies.services;

import android.os.AsyncTask;
import android.text.TextUtils;
import android.util.Log;

import com.alessiogrumiro.udacity.popularmovies.enums.MoviesSortByEnum;
import com.alessiogrumiro.udacity.popularmovies.exceptions.MissingApiKeyException;
import com.alessiogrumiro.udacity.popularmovies.listeners.OnLoadingMoviesListener;
import com.alessiogrumiro.udacity.popularmovies.models.Movie;
import com.alessiogrumiro.udacity.popularmovies.services.models.ConfigResponseContainer;
import com.alessiogrumiro.udacity.popularmovies.services.models.MovieDb;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


/**
 * Created by Alessio Grumiro on 25/01/17.
 */

public class MovieService implements IMovieService {

    private static final String TAG = "MovieService";

    private List<Movie> mMovies;
    private MoviesSortByEnum mLastSortBy;
    private SimpleDateFormat mSimpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
    private TmdbApiClient mApiClient;

    public MovieService() {
        mApiClient = new TmdbApiClient();
    }

    public void setApiKey(String apiKey) {
        mApiClient.setApiKey(apiKey);
    }

    public String getApiKey() {
        return mApiClient.getApiKey();
    }

    public void setLanguage(String language) {
        mApiClient.setLanguage(language);
    }

    @Override
    public void getMovies(MoviesSortByEnum sortby, OnLoadingMoviesListener listener) {
        getMovies(sortby, false, listener);
    }

    @Override
    public void getMovies(MoviesSortByEnum sortby, boolean forceRefresh,
                          final OnLoadingMoviesListener listener) {
        if (listener == null)
            throw new IllegalArgumentException("You have to set a listener for results");

        if (sortby == mLastSortBy && !forceRefresh && mMovies != null)
            listener.onLoadingComplete(mMovies);

        mLastSortBy = sortby;

        new AsyncTask<Void, Void, List<Movie>>() {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                // TODO check network status

                if (listener != null) listener.onLoadingStart();
            }

            @Override
            protected List<Movie> doInBackground(Void... params) {
                String sortbyValue = TmdbApiClient.SORTBY_TOP_RATED;
                switch (mLastSortBy) {
                    case MostPopular:
                        sortbyValue = TmdbApiClient.SORTBY_MOST_POPULAR;
                        break;
                    default:
                }

                try {
                    List<List<MovieDb>> movieLists = new ArrayList<List<MovieDb>>(5);
                    movieLists.add(mApiClient.getMovies(sortbyValue, 1));
                    movieLists.add(mApiClient.getMovies(sortbyValue, 2));
                    movieLists.add(mApiClient.getMovies(sortbyValue, 3));
                    movieLists.add(mApiClient.getMovies(sortbyValue, 4));
                    movieLists.add(mApiClient.getMovies(sortbyValue, 5));

                    List<MovieDb> allMovies = new ArrayList<MovieDb>();
                    for (List<MovieDb> l : movieLists) {
                        allMovies.addAll(l);
                    }

                    if (!allMovies.isEmpty()) {
                        mMovies = new ArrayList<>(allMovies.size());
                        ConfigResponseContainer config = mApiClient.getConfig();
                        for (MovieDb movieDb : allMovies) {
                            String completePosterUrl = String.format("%s%s%s",
                                    config.getImageParams().getBaseUrlSecure(),
                                    config.getImageParams().getPosterSizes().get(2),
                                    movieDb.getPosterUrl()
                            );

                            Movie movie = new Movie();
                            movie.setId(movieDb.getId());
                            movie.setOverview(movieDb.getOverview());
                            movie.setPosterUrl(completePosterUrl);
                            movie.setTitle(movieDb.getTitle());
                            movie.setVoteAverage(movieDb.getVoteAverage());
                            movie.setVoteCount(movieDb.getVoteCount());
                            if (!TextUtils.isEmpty(movieDb.getReleaseDate())) {
                                Calendar calendar = Calendar.getInstance();
                                calendar.setTime(mSimpleDateFormat.parse(movieDb.getReleaseDate()));
                                movie.setYear(calendar.get(Calendar.YEAR));
                            }
                            mMovies.add(movie);
                        }
                    }
                } catch (MissingApiKeyException e) {
                    Log.e(TAG, e.getMessage(), e);
                    // TODO handle MissingApiKeyException
                } catch (Exception e) {
                    Log.e(TAG, e.getMessage(), e);
                }
                return mMovies;
            }

            @Override
            protected void onPostExecute(List<Movie> movies) {
                super.onPostExecute(movies);
                if (listener != null) listener.onLoadingComplete(movies);
            }
        }.execute();
    }
//
//    @Override
//    public MovieDb getMovie(int id) throws MissingApiKeyException {
//        MovieDb result = null;
//        if (mMovies != null) {
//            TmdbMovies moviesContainer = mApiClient.getMovies();
//            if (moviesContainer != null)
//                result = moviesContainer.getMovie(id, mDefaultLocale);
//        }
//        return result;
//    }
}
