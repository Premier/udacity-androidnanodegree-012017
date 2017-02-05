package com.alessiogrumiro.udacity.popularmovies;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.alessiogrumiro.udacity.popularmovies.models.Movie;
import com.squareup.picasso.Picasso;

public class MovieDetailsActivity extends AppCompatActivity {

    private static final String EXTRA_MOVIE = "movie";
    private static final String TAG = "MovieDetailsActivity";

    // UI
    private TextView mTitleView;
    private TextView mReleaseYearView;
    private TextView mDurationView;
    private TextView mRateView;
    private TextView mOverviewView;
    private ImageView mPosterView;

    // other
    private Movie mCurrentMovie;

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

        mTitleView = (TextView) findViewById(android.R.id.title);
        mReleaseYearView = (TextView) findViewById(android.R.id.text1);
        mDurationView = (TextView) findViewById(android.R.id.text2);
        mRateView = (TextView) findViewById(R.id.text3);
        mOverviewView = (TextView) findViewById(R.id.text4);
        mPosterView = (ImageView) findViewById(android.R.id.icon);

        Intent data = getIntent();
        if (data != null && data.hasExtra(EXTRA_MOVIE) && (mCurrentMovie = data.getParcelableExtra(EXTRA_MOVIE)) != null) {
            mTitleView.setText(mCurrentMovie.getTitle());
            mReleaseYearView.setText(String.valueOf(mCurrentMovie.getYear()));
            mDurationView.setText(String.format("%dmin", mCurrentMovie.getDuration()));
            mRateView.setText(String.format("%.2f / %d", mCurrentMovie.getVoteAverage(), mCurrentMovie.getVoteCount()));
            mOverviewView.setText(mCurrentMovie.getOverview());
            Picasso.with(this).load(mCurrentMovie.getPosterUrl()).into(mPosterView);
        } else {
            Toast.makeText(this, R.string.error_no_data, Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }
}
