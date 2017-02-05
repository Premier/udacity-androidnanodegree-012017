package com.alessiogrumiro.udacity.popularmovies.services;

import com.alessiogrumiro.udacity.popularmovies.enums.MoviesSortByEnum;
import com.alessiogrumiro.udacity.popularmovies.listeners.OnLoadingMoviesListener;


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
    void getMovies(MoviesSortByEnum sortby, final OnLoadingMoviesListener listener);

    /**
     * Fetch movies catalog
     * @return
     */
    void getMovies(MoviesSortByEnum sortby, boolean forceRefresh, final OnLoadingMoviesListener listener);

    /**
     * Fetch details for specific movie. It detects device language to retrieve info in user language.
     * @param id
     * @return
     */
    //Movie getMovie(int id);
}
