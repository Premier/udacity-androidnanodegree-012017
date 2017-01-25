package com.alessiogrumiro.udacity.popularmovies.services;

import com.alessiogrumiro.udacity.popularmovies.exceptions.MissingApiKeyException;

import info.movito.themoviedbapi.TmdbMovies;
import info.movito.themoviedbapi.model.MovieDb;

/**
 * Created by Alessio Grumiro on 25/01/17.
 */

public interface IMovieService {

    /**
     * Set current apikey
     * @param apikey
     */
    void setApiKey(String apikey);

    /**
     * Fetch movies catalog
     * @return
     */
    TmdbMovies getMovies() throws MissingApiKeyException;

    /**
     * Fetch movies catalog
     * @return
     */
    TmdbMovies getMovies(boolean forceRefresh) throws MissingApiKeyException;

    /**
     * Fetch details for specific movie. It detects device language to retrieve info in user language.
     * @param id
     * @return
     */
    MovieDb getMovie(int id) throws MissingApiKeyException;
}
