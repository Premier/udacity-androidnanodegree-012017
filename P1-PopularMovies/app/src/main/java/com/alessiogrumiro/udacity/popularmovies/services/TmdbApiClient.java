package com.alessiogrumiro.udacity.popularmovies.services;

import android.text.TextUtils;

import com.alessiogrumiro.udacity.popularmovies.exceptions.MissingApiKeyException;
import com.alessiogrumiro.udacity.popularmovies.services.models.ConfigResponseContainer;
import com.alessiogrumiro.udacity.popularmovies.services.models.DiscoverResponseContainer;
import com.alessiogrumiro.udacity.popularmovies.services.models.MovieDb;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.Callable;

import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by Alessio Grumiro on 05/02/17.
 */

public class TmdbApiClient {

    public static final String SORTBY_TOP_RATED = "vote_average.desc";
    public static final String SORTBY_MOST_POPULAR = "popularity.desc";

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
        this(null, Locale.getDefault().getDisplayLanguage());
    }

    public TmdbApiClient(String apiKey) {
        this(apiKey, Locale.getDefault().getDisplayLanguage());
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

    public List<MovieDb> getMovies(String sortby, int page) throws IOException, MissingApiKeyException {
        if (TextUtils.isEmpty(mApiKey)) throw new MissingApiKeyException();
        List<MovieDb> movies = null;

        if (TextUtils.isEmpty(sortby)) sortby = SORTBY_MOST_POPULAR;
        if (page < 1) page = 1;
        if (page > 1000) page = 1000;

        OkHttpClient client = new OkHttpClient();

        HttpUrl.Builder urlBuilder = HttpUrl.parse(sBaseUrl).newBuilder();
        urlBuilder.addPathSegments("discover/movie");
        urlBuilder.addQueryParameter(sLanguageParamName, mDefaultLanguage);
        urlBuilder.addQueryParameter(sApiKeyParamName, mApiKey);
        urlBuilder.addQueryParameter(sSortbyParamName, sortby);
        urlBuilder.addQueryParameter(sPageParamName, String.valueOf(page));

        String url = urlBuilder.build().toString();

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
}
