package com.alessiogrumiro.udacity.popularmovies.services.adapters;

import android.text.TextUtils;
import android.util.Log;

import com.alessiogrumiro.udacity.popularmovies.models.Movie;
import com.alessiogrumiro.udacity.popularmovies.services.models.ConfigResponseContainer;
import com.alessiogrumiro.udacity.popularmovies.services.models.MovieDb;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by Alessio Grumiro on 05/02/17.
 */

public class MovieAdapter {

    private static final String TAG = "MovieAdapter";

    private static SimpleDateFormat mSimpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");

    public static List<Movie> toMovie(List<MovieDb> movieDbs, ConfigResponseContainer config) {
        List<Movie> results = null;
        if (movieDbs != null) {
            results = new ArrayList<>(movieDbs.size());
            for (MovieDb m : movieDbs) results.add(toMovie(m, config));
        }
        return results;
    }

    public static Movie toMovie(MovieDb movieDb, ConfigResponseContainer config) {
        Movie result = null;
        if (movieDb != null) {
            String completePosterUrl = String.format("%s%s%s",
                    config.getImageParams().getBaseUrlSecure(),
                    config.getImageParams().getPosterSizes().get(2),
                    movieDb.getPosterUrl()
            );

            result = new Movie();
            result.setId(movieDb.getId());
            result.setOverview(movieDb.getOverview());
            result.setPosterUrl(completePosterUrl);
            result.setTitle(movieDb.getTitle());
            result.setVoteAverage(movieDb.getVoteAverage());
            result.setVoteCount(movieDb.getVoteCount());
            if (!TextUtils.isEmpty(movieDb.getReleaseDate())) {
                Calendar calendar = Calendar.getInstance();
                try {
                    calendar.setTime(mSimpleDateFormat.parse(movieDb.getReleaseDate()));
                    result.setYear(calendar.get(Calendar.YEAR));
                } catch (ParseException e) {
                    Log.e(TAG, e.getMessage(), e);
                }
            }
        }
        return result;
    }
}
