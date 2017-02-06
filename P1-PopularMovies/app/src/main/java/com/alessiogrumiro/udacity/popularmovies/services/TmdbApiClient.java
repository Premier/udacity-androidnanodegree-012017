package com.alessiogrumiro.udacity.popularmovies.services;

import android.text.TextUtils;
import android.util.Log;

import com.alessiogrumiro.udacity.popularmovies.enums.MoviesSortByEnum;
import com.alessiogrumiro.udacity.popularmovies.exceptions.MissingApiKeyException;
import com.alessiogrumiro.udacity.popularmovies.services.models.ConfigResponseContainer;
import com.alessiogrumiro.udacity.popularmovies.services.models.DiscoverResponseContainer;
import com.alessiogrumiro.udacity.popularmovies.services.models.MovieDb;
import com.alessiogrumiro.udacity.popularmovies.services.models.MoviesResponseContainer;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by Alessio Grumiro on 05/02/17.
 */

public class TmdbApiClient {

    public static final String DISCOVER_SORTBY_TOP_RATED = "vote_average.desc";
    public static final String DISCOVER_SORTBY_MOST_POPULAR = "popularity.desc";
    public static final String MOVIES_SORTBY_TOP_RATED = "top_rated";
    public static final String MOVIES_SORTBY_MOST_POPULAR = "popular";

    private static final String TAG = "TmdbApiClient";
    private static final String sApiKeyParamName = "api_key";
    private static final String sBaseUrl = "https://api.themoviedb.org/3";
    private static final String sLanguageParamName = "language";
    private static final String sSortbyParamName = "sort_by";
    private static final String sPageParamName = "page";
    private String mApiKey;
    private String mDefaultLanguage;
    private Gson gson = new Gson();
    private ConfigResponseContainer mConfig;

    public TmdbApiClient() {
        this(null, Locale.getDefault().getLanguage());
    }

    public TmdbApiClient(String apiKey) {
        this(apiKey, Locale.getDefault().getLanguage());
    }

    public TmdbApiClient(String apiKey, String isoLanguage) {
        mApiKey = apiKey;
        mDefaultLanguage = isoLanguage;
    }

    public String getApiKey() {
        return mApiKey;
    }

    public void setApiKey(String apikey) {
        mApiKey = apikey;
    }

    public void setLanguage(String language) {
        mDefaultLanguage = language;
    }

    public synchronized ConfigResponseContainer getConfig() throws IOException, MissingApiKeyException {
        if (mConfig == null) {
            if (TextUtils.isEmpty(mApiKey)) throw new MissingApiKeyException();
            OkHttpClient client = new OkHttpClient();

            HttpUrl.Builder urlBuilder = HttpUrl.parse(sBaseUrl).newBuilder();
            urlBuilder.addPathSegments("configuration");
            urlBuilder.addQueryParameter(sApiKeyParamName, mApiKey);
            String url = urlBuilder.build().toString();
            Log.d(TAG, String.format("Calling url -> %s", url));

            Request request = new Request.Builder()
                    .url(url)
                    .build();

            Response response = client.newCall(request).execute();
            if (response != null && response.isSuccessful()) {
                String responseData = response.body().string();
                mConfig = gson.fromJson(responseData, ConfigResponseContainer.class);
            }
        }

        return mConfig;
    }

    public List<MovieDb> discover(MoviesSortByEnum sortby, int page) throws IOException, MissingApiKeyException {
        if (TextUtils.isEmpty(mApiKey)) throw new MissingApiKeyException();
        List<MovieDb> movies = null;

        String sortByValue =
                sortby == null || sortby == MoviesSortByEnum.MostPopular
                        ? DISCOVER_SORTBY_MOST_POPULAR
                        : DISCOVER_SORTBY_TOP_RATED;
        if (page < 1) page = 1;
        if (page > 1000) page = 1000;

        OkHttpClient client = new OkHttpClient();

        HttpUrl.Builder urlBuilder = HttpUrl.parse(sBaseUrl).newBuilder();
        urlBuilder.addPathSegments("discover/movie");
        urlBuilder.addQueryParameter(sLanguageParamName, mDefaultLanguage);
        urlBuilder.addQueryParameter(sApiKeyParamName, mApiKey);
        urlBuilder.addQueryParameter(sSortbyParamName, sortByValue);
        urlBuilder.addQueryParameter(sPageParamName, String.valueOf(page));

        String url = urlBuilder.build().toString();
        Log.d(TAG, String.format("Calling url -> %s", url));

        Request request = new Request.Builder()
                .url(url)
                .build();

        Response response = client.newCall(request).execute();
        if (response != null && response.isSuccessful()) {
            String responseData = response.body().string();
            DiscoverResponseContainer responseContainer = gson.fromJson(responseData, DiscoverResponseContainer.class);
            movies = responseContainer.getResults();
        }
        return movies;
    }

    public List<MovieDb> movies(MoviesSortByEnum sortby, int page) throws IOException, MissingApiKeyException {
        if (TextUtils.isEmpty(mApiKey)) throw new MissingApiKeyException();
        List<MovieDb> movies = null;

        String sortByValue =
                sortby == null || sortby == MoviesSortByEnum.MostPopular
                        ? MOVIES_SORTBY_MOST_POPULAR
                        : MOVIES_SORTBY_TOP_RATED;
        if (page < 1) page = 1;
        if (page > 1000) page = 1000;

        OkHttpClient client = new OkHttpClient();

        HttpUrl.Builder urlBuilder = HttpUrl.parse(sBaseUrl).newBuilder();
        urlBuilder.addPathSegments(String.format("movie/%s", sortByValue));
        urlBuilder.addQueryParameter(sLanguageParamName, mDefaultLanguage);
        urlBuilder.addQueryParameter(sApiKeyParamName, mApiKey);
        urlBuilder.addQueryParameter(sPageParamName, String.valueOf(page));

        String url = urlBuilder.build().toString();
        Log.d(TAG, String.format("Calling url -> %s", url));

        Request request = new Request.Builder()
                .url(url)
                .build();

        Response response = client.newCall(request).execute();
        if (response != null && response.isSuccessful()) {
            String responseData = response.body().string();
            MoviesResponseContainer responseContainer = gson.fromJson(responseData, MoviesResponseContainer.class);
            movies = responseContainer.getResults();
        }
        return movies;
    }

    public MovieDb movie(int id) throws IOException, MissingApiKeyException {
        if (TextUtils.isEmpty(mApiKey)) throw new MissingApiKeyException();
        MovieDb movie = null;
        OkHttpClient client = new OkHttpClient();

        HttpUrl.Builder urlBuilder = HttpUrl.parse(sBaseUrl).newBuilder();
        urlBuilder.addPathSegments(String.format("movie/%d", id));
        urlBuilder.addQueryParameter(sLanguageParamName, mDefaultLanguage);
        urlBuilder.addQueryParameter(sApiKeyParamName, mApiKey);

        String url = urlBuilder.build().toString();
        Log.d(TAG, String.format("Calling url -> %s", url));

        Request request = new Request.Builder()
                .url(url)
                .build();

        Response response = client.newCall(request).execute();
        if (response != null && response.isSuccessful()) {
            String responseData = response.body().string();
            movie = gson.fromJson(responseData, MovieDb.class);
        }
        return movie;
    }
}
