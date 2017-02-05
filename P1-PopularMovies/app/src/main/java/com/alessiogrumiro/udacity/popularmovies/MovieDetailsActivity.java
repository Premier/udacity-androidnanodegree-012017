package com.alessiogrumiro.udacity.popularmovies;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.alessiogrumiro.udacity.popularmovies.models.Movie;

public class MovieDetailsActivity extends AppCompatActivity {

    private static final String EXTRA_MOVIE = "movie";
    private static final String TAG = "MovieDetailsActivity";

    public static Intent newIntent(Context context, Movie movie) {
        Intent i = null;
        try {
            i = new Intent(context, MovieDetailsActivity.class);
            i.putExtra(EXTRA_MOVIE, movie);
        } catch (Exception e) {
            Log.e(TAG, e.getMessage(), e);
        }
        return i;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);
    }
}
