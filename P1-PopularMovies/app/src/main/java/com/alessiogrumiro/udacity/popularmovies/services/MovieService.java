package com.alessiogrumiro.udacity.popularmovies.services;

import android.text.TextUtils;

import com.alessiogrumiro.udacity.popularmovies.MovieApplication;
import com.alessiogrumiro.udacity.popularmovies.enums.MoviesSortByEnum;
import com.alessiogrumiro.udacity.popularmovies.exceptions.MissingApiKeyException;

import java.util.List;
import java.util.Locale;

import info.movito.themoviedbapi.TmdbApi;
import info.movito.themoviedbapi.TmdbMovies;
import info.movito.themoviedbapi.model.MovieDb;
import info.movito.themoviedbapi.model.core.MovieResultsPage;

/**
 * Created by Alessio Grumiro on 25/01/17.
 */

public class MovieService implements IMovieService {

    public static final String PREF_API_KEY = "themoviedb_apikey";
    private String mApiKey;
    private TmdbApi mApiClient;
    private List<MovieDb> mMovies;
    private MoviesSortByEnum mLastSortBy;
    private String mDefaultLocale;

    public MovieService() {
        mDefaultLocale = Locale.getDefault().getDisplayLanguage();
        mApiKey = MovieApplication.getPreferences().getString(PREF_API_KEY, "");
        if (!TextUtils.isEmpty(mApiKey)) mApiClient = new TmdbApi(mApiKey);
    }

    @Override
    public void setApiKey(String apikey) {
        mApiKey = apikey;
        if (!TextUtils.isEmpty(mApiKey)) mApiClient = new TmdbApi(mApiKey);
    }

    @Override
    public List<MovieDb> getMovies(MoviesSortByEnum sortby) throws MissingApiKeyException {
        return getMovies(sortby, false);
    }

    @Override
    public List<MovieDb> getMovies(MoviesSortByEnum sortby, boolean forceRefresh) throws MissingApiKeyException {
        if (TextUtils.isEmpty(mApiKey)) throw new MissingApiKeyException();
        if (mMovies == null || sortby != mLastSortBy || forceRefresh) {
            mLastSortBy = sortby;
            TmdbMovies moviesContainer = mApiClient.getMovies();
            if (moviesContainer != null) {
                switch (mLastSortBy) {
                    case TopRated: {
                        MovieResultsPage page = moviesContainer.getTopRatedMovies(mDefaultLocale, 0);
                        if (page != null)
                            mMovies = page.getResults();
                        break;
                    }
                    default: {
                        MovieResultsPage page = moviesContainer.getNowPlayingMovies(mDefaultLocale, 0);
                        if (page != null)
                            mMovies = page.getResults();
                    }
                }
            }
        }

        return mMovies;
    }

    @Override
    public MovieDb getMovie(int id) throws MissingApiKeyException {
        MovieDb result = null;
        if (mMovies != null) {
            TmdbMovies moviesContainer = mApiClient.getMovies();
            if (moviesContainer != null)
                result = moviesContainer.getMovie(id, mDefaultLocale);
        }
        return result;
    }
}
