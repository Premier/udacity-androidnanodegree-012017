package com.alessiogrumiro.udacity.popularmovies;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewStub;
import android.widget.Button;

import com.alessiogrumiro.udacity.popularmovies.adapters.MovieAdapter;
import com.alessiogrumiro.udacity.popularmovies.enums.MoviesSortByEnum;
import com.alessiogrumiro.udacity.popularmovies.listeners.OnLoadingMoviesListener;
import com.alessiogrumiro.udacity.popularmovies.listeners.OnMovieClickListener;
import com.alessiogrumiro.udacity.popularmovies.models.Movie;
import com.alessiogrumiro.udacity.popularmovies.services.IMovieService;

import java.util.List;

public class HomeActivity extends AppCompatActivity implements OnMovieClickListener, View.OnClickListener {

    private static final String TAG = "HomeActivity";
    private static final int REQCODE_SETTINGS = 100;

    // UI
    private RecyclerView mMoviesView;
    private Button mRefreshButton;
    private View mLoadingView;
    private View mNodataView;

    //
    private MoviesSortByEnum mCurrentMovieSort = MoviesSortByEnum.MostPopular;
    private MovieAdapter mAdapter;
    private IMovieService mMovieService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mMovieService = MovieApplication.getMovieService();

        // binding UI
        setContentView(R.layout.activity_home);
        mMoviesView = (RecyclerView)findViewById(R.id.rv_movies);
        ViewStub emptyView = (ViewStub) findViewById(android.R.id.empty);

        View inflatedView = emptyView.inflate();
        mRefreshButton = (Button) inflatedView.findViewById(android.R.id.button1);
        mLoadingView = inflatedView.findViewById(android.R.id.progress);
        mNodataView = inflatedView.findViewById(R.id.nodata_layout);

        mRefreshButton.setOnClickListener(this);

        // init recyclerview
        int spanCount = getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE ? 4 : 2;
        GridLayoutManager layoutManager = new GridLayoutManager(this, spanCount);
        mMoviesView.setLayoutManager(layoutManager);

        mMoviesView.setHasFixedSize(true);
        mAdapter = new MovieAdapter(this, this);

        mMoviesView.setAdapter(mAdapter);

    }

    private void loadMovies() {
        mMovieService.getMovies(mCurrentMovieSort, new OnLoadingMoviesListener() {
            @Override
            public void onLoadingStart() {
                mMoviesView.setVisibility(View.GONE);
                mLoadingView.setVisibility(View.VISIBLE);
                mNodataView.setVisibility(View.GONE);
            }

            @Override
            public void onLoadingComplete(List<Movie> movies) {
                if (movies != null && movies.size() > 0) {
                    mAdapter.setData(movies);
                    mLoadingView.setVisibility(View.GONE);
                    mNodataView.setVisibility(View.GONE);
                    mMoviesView.setVisibility(View.VISIBLE);
                    mMoviesView.scrollToPosition(0);
                } else {
                    mLoadingView.setVisibility(View.GONE);
                    mNodataView.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (TextUtils.isEmpty(mMovieService.getApiKey())) {
            String apiKey = getSharedPreferences(MovieApplication.PREF_APIKEY, MODE_PRIVATE).getString(MovieApplication.PREF_APIKEY, "");
            if (TextUtils.isEmpty(apiKey)) {
                startActivityForResult(SettingsActivity.newIntent(this), REQCODE_SETTINGS);
            } else {
                mMovieService.setApiKey(apiKey);
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQCODE_SETTINGS && resultCode == RESULT_OK) {
            String newApiKey = data.getStringExtra(SettingsActivity.EXTRA_APIKEY);
            String apiKey = getSharedPreferences(MovieApplication.PREF_APIKEY, MODE_PRIVATE).getString(MovieApplication.PREF_APIKEY, "");
            if (!TextUtils.equals(apiKey, newApiKey)) {
                getSharedPreferences(MovieApplication.PREF_APIKEY, MODE_PRIVATE)
                        .edit()
                        .putString(MovieApplication.PREF_APIKEY, newApiKey)
                        .commit();
                mMovieService.setApiKey(newApiKey);
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadMovies();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        MoviesSortByEnum newSort = mCurrentMovieSort;
        switch (item.getItemId()) {
            case R.id.action_sortby_rate:
                newSort = MoviesSortByEnum.TopRated;
                break;
            case R.id.action_sortby_mostpopular:
                newSort = MoviesSortByEnum.MostPopular;
                break;
            case R.id.action_settings:
                String apiKey = getSharedPreferences(MovieApplication.PREF_APIKEY, MODE_PRIVATE).getString(MovieApplication.PREF_APIKEY, "");
                startActivityForResult(SettingsActivity.newIntent(this, apiKey), REQCODE_SETTINGS);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
        if (newSort != mCurrentMovieSort) {
            mCurrentMovieSort = newSort;
            loadMovies();
        }
        return true;
    }

    @Override
    public void onMovieClick(long id, Movie movie) {
        startActivity(MovieDetailsActivity.newIntent(this, movie));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case android.R.id.button1: {
                loadMovies();
                break;
            }
        }
    }
}
