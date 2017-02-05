package com.alessiogrumiro.udacity.popularmovies;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import com.alessiogrumiro.udacity.popularmovies.services.IMovieService;
import com.alessiogrumiro.udacity.popularmovies.services.MovieService;

/**
 * Created by Alessio Grumiro on 25/01/17.
 */

public class MovieApplication extends Application {

    public static final String PREF_NAME = "ag_movieapp";
    public static final String PREF_APIKEY = "pref_apikey";
    private static IMovieService sMovieService;

    private static Application sInstance;

    public MovieApplication(){
        sInstance = this;
    }

    public static IMovieService getMovieService(){
        if (sMovieService == null)
            sMovieService = new MovieService();
        return sMovieService;
    }

    public static Application getInstance(){
        // TODO handle singleton
        return sInstance;
    }

    public static SharedPreferences getPreferences(){
        // TODO handle singleton
        return MovieApplication.getInstance().getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    }
}
