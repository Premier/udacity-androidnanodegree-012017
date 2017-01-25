package com.alessiogrumiro.udacity.popularmovies;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Alessio Grumiro on 25/01/17.
 */

public class MovieApplication extends Application {

    public static final String PREF_NAME = "ag_movieapp";

    private static Application sInstance;

    public MovieApplication(){
        sInstance = this;
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
