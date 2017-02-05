package com.alessiogrumiro.udacity.popularmovies.services;

import com.alessiogrumiro.udacity.popularmovies.enums.MoviesSortByEnum;
import com.alessiogrumiro.udacity.popularmovies.listeners.OnMoviesLoadListener;


/**
 * Created by Alessio Grumiro on 25/01/17.
 */

public interface IMovieService {

    /**
     * Set service apikey for api call
     * @param apiKey
     */
    void setApiKey(String apiKey);

    /**
     *
     * @return
     */
    String getApiKey();

    /**
     * Set language for movies list
     * @param language
     */
    void setLanguage(String language);

    /**
     * Fetch movies catalog
     * @return
     */
    void getMovies(MoviesSortByEnum sortby, final OnMoviesLoadListener listener);

    /**
     * Fetch movies catalog
     * @return
     */
    void getMovies(MoviesSortByEnum sortby, boolean forceRefresh, final OnMoviesLoadListener listener);
}
