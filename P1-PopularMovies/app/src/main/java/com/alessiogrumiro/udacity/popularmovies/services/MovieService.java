package com.alessiogrumiro.udacity.popularmovies.services;

import android.text.TextUtils;

import com.alessiogrumiro.udacity.popularmovies.MovieApplication;
import com.alessiogrumiro.udacity.popularmovies.exceptions.MissingApiKeyException;

import java.util.Locale;

import info.movito.themoviedbapi.TmdbApi;
import info.movito.themoviedbapi.TmdbMovies;
import info.movito.themoviedbapi.model.MovieDb;

/**
 * Created by Alessio Grumiro on 25/01/17.
 */

public class MovieService implements IMovieService {

    public static final String PREF_API_KEY = "themoviedb_apikey";
    private String mApiKey;
    private TmdbApi mApiClient;
    private TmdbMovies mMovies;
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
    public TmdbMovies getMovies() throws MissingApiKeyException {
        return getMovies(false);
    }

    @Override
    public TmdbMovies getMovies(boolean forceRefresh) throws MissingApiKeyException {
        if (TextUtils.isEmpty(mApiKey)) throw new MissingApiKeyException();
        if (mMovies == null || forceRefresh)
            mMovies = mApiClient.getMovies();
        return mMovies;
    }

    @Override
    public MovieDb getMovie(int id) throws MissingApiKeyException {
        MovieDb result = null;
        if (mMovies != null) result = mMovies.getMovie(id, mDefaultLocale);
        return result;
    }
}
