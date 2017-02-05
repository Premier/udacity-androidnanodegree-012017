package com.alessiogrumiro.udacity.popularmovies.listeners;

import com.alessiogrumiro.udacity.popularmovies.models.Movie;

/**
 * Created by Alessio Grumiro on 25/01/17.
 */

public interface OnMovieClickListener {
    void onMovieClick(long id, Movie movie);
}