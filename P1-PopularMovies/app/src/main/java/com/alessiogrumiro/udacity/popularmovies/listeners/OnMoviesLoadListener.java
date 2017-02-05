package com.alessiogrumiro.udacity.popularmovies.listeners;

import com.alessiogrumiro.udacity.popularmovies.models.Movie;

import java.util.List;

/**
 * Created by Alessio Grumiro on 27/01/17.
 */

public interface OnMoviesLoadListener {

    void onMoviesLoadStart();
    void onMoviesLoadComplete(List<Movie> movies);
    void onMoviesLoadFail(Exception e);
}
