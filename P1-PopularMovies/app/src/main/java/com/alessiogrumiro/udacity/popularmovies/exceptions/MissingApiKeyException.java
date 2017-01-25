package com.alessiogrumiro.udacity.popularmovies.exceptions;

/**
 * Created by Alessio Grumiro on 25/01/17.
 */

public class MissingApiKeyException extends Exception {

    public MissingApiKeyException(){
        super("Missing api key, unable to access to themoviedb.org");
    }
}
