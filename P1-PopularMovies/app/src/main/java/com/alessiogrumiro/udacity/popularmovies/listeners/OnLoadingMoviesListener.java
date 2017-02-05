package com.alessiogrumiro.udacity.popularmovies.listeners;

import com.alessiogrumiro.udacity.popularmovies.models.Movie;

import java.util.List;

/**
 * Created by Alessio Grumiro on 27/01/17.
 */

public interface OnLoadingMoviesListener {

    void onLoadingStart();
    void onLoadingComplete(List<Movie> movies);
}
