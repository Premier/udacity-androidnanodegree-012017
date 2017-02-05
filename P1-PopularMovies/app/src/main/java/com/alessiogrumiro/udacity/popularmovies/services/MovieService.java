package com.alessiogrumiro.udacity.popularmovies.services;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.util.Log;

import com.alessiogrumiro.udacity.popularmovies.MovieApplication;
import com.alessiogrumiro.udacity.popularmovies.enums.MoviesSortByEnum;
import com.alessiogrumiro.udacity.popularmovies.exceptions.MissingApiKeyException;
import com.alessiogrumiro.udacity.popularmovies.listeners.OnMoviesLoadListener;
import com.alessiogrumiro.udacity.popularmovies.models.Movie;
import com.alessiogrumiro.udacity.popularmovies.services.adapters.MovieAdapter;
import com.alessiogrumiro.udacity.popularmovies.services.models.ConfigResponseContainer;
import com.alessiogrumiro.udacity.popularmovies.services.models.MovieDb;
import com.alessiogrumiro.udacity.popularmovies.utils.AsyncTaskResult;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;


/**
 * Created by Alessio Grumiro on 25/01/17.
 */

public class MovieService implements IMovieService {

    private static final String TAG = "MovieService";

    private List<Movie> mMovies;
    private MoviesSortByEnum mLastSortBy;
    private TmdbApiClient mApiClient;
    private ConnectivityManager mConnectivityManager;

    public MovieService() {
        mApiClient = new TmdbApiClient();
        mConnectivityManager = (ConnectivityManager) MovieApplication.getInstance().getSystemService(Context.CONNECTIVITY_SERVICE);
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

    private String getSortByString(MoviesSortByEnum enumValue) {
        switch (enumValue) {
            case MostPopular:
                return TmdbApiClient.SORTBY_MOST_POPULAR;
            default:
                return TmdbApiClient.SORTBY_TOP_RATED;
        }
    }

    @Override
    public void getMovies(MoviesSortByEnum sortby, OnMoviesLoadListener listener) {
        getMovies(sortby, false, listener);
    }

    @Override
    public void getMovies(MoviesSortByEnum sortby, boolean forceRefresh,
                          final OnMoviesLoadListener listener) {
        if (listener == null)
            throw new IllegalArgumentException("You have to set a listener for results");

        if (sortby == mLastSortBy && !forceRefresh && mMovies != null) {
            listener.onMoviesLoadComplete(mMovies);
            return;
        }

        mLastSortBy = sortby;

        new AsyncTask<Void, Void, AsyncTaskResult<List<Movie>>>() {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                NetworkInfo networkInfo = mConnectivityManager.getActiveNetworkInfo();
                if (networkInfo == null || !networkInfo.isConnected()) {
                    listener.onMoviesLoadFail(new Exception("No network, please connect and retry"));
                    cancel(true);
                } else {
                    listener.onMoviesLoadStart();
                }
            }

            @Override
            protected AsyncTaskResult<List<Movie>> doInBackground(Void... params) {
                List<Movie> movies = null;
                try {
                    final String sortbyString = getSortByString(mLastSortBy);

                    ExecutorService executorService = Executors.newFixedThreadPool(5);
                    List<Callable<List<MovieDb>>> callables = new ArrayList<>();
                    for (int i : new int[]{1, 2, 3, 4, 5}) {
                        final int pageIndex = i;
                        callables.add(new Callable<List<MovieDb>>() {
                            @Override
                            public List<MovieDb> call() throws Exception {
                                return mApiClient.getMovies(sortbyString, pageIndex);
                            }
                        });
                    }
                    List<Future<List<MovieDb>>> futures = executorService.invokeAll(callables);

                    List<MovieDb> allMovies = new ArrayList<>();
                    for (Future<List<MovieDb>> f : futures) {
                        List<MovieDb> results = f.get();
                        if (results != null && results.size() > 0)
                            allMovies.addAll(results);
                    }
                    executorService.shutdown();

                    if (!allMovies.isEmpty()) {
                        ConfigResponseContainer config = mApiClient.getConfig();
                        movies = MovieAdapter.toMovie(allMovies, config);
                    }
                } catch (MissingApiKeyException e) {
                    Log.e(TAG, e.getMessage(), e);
                    return new AsyncTaskResult<>(e);
                } catch (Exception e) {
                    Log.e(TAG, e.getMessage(), e);
                    return new AsyncTaskResult<>(e);
                }
                return new AsyncTaskResult<>(movies);
            }

            @Override
            protected void onPostExecute(AsyncTaskResult<List<Movie>> result) {
                super.onPostExecute(result);
                if (result.getError() != null) {
                    listener.onMoviesLoadFail(result.getError());
                } else {
                    mMovies = result.getResult();
                    listener.onMoviesLoadComplete(result.getResult());
                }
            }
        }.execute();
    }
}
