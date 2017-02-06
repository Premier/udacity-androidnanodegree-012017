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
import android.widget.Toast;

import com.alessiogrumiro.udacity.popularmovies.adapters.MovieAdapter;
import com.alessiogrumiro.udacity.popularmovies.enums.MoviesSortByEnum;
import com.alessiogrumiro.udacity.popularmovies.listeners.OnMovieClickListener;
import com.alessiogrumiro.udacity.popularmovies.listeners.OnMoviesLoadListener;
import com.alessiogrumiro.udacity.popularmovies.models.Movie;
import com.alessiogrumiro.udacity.popularmovies.services.IMovieService;

import java.util.List;

public class HomeActivity extends AppCompatActivity implements OnMovieClickListener, View.OnClickListener {

    private static final String TAG = "HomeActivity";
    private static final int REQCODE_SETTINGS = 100;
    private static final String EXTRA_LAST_SORTBY = "extra_lastsortby";
    private static final String EXTRA_LISTSTATE = "extra_liststate";

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

        // init recyclerview
        mAdapter = new MovieAdapter(this, this);

        mMoviesView.setAdapter(mAdapter);
        int spanCount = getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE ? 4 : 2;
        GridLayoutManager layoutManager = new GridLayoutManager(this, spanCount);

        mMoviesView.setLayoutManager(layoutManager);
        mMoviesView.setHasFixedSize(true);
    }

    private void loadMovies(final boolean reload) {
        mMovieService.getMovies(mCurrentMovieSort, reload, new OnMoviesLoadListener() {
            @Override
            public void onMoviesLoadStart() {
                mMoviesView.setVisibility(View.GONE);
                mLoadingView.setVisibility(View.VISIBLE);
                mNodataView.setVisibility(View.GONE);
            }

            @Override
            public void onMoviesLoadComplete(List<Movie> movies) {
                if (movies != null && movies.size() > 0) {
                    mAdapter.setData(movies);
                    mLoadingView.setVisibility(View.GONE);
                    mNodataView.setVisibility(View.GONE);
                    mMoviesView.setVisibility(View.VISIBLE);
                    if (reload)
                    mMoviesView.scrollToPosition(0);
                } else {
                    mLoadingView.setVisibility(View.GONE);
                    mNodataView.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onMoviesLoadFail(Exception e) {
                mLoadingView.setVisibility(View.GONE);
                Toast.makeText(HomeActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                if (mMoviesView.getAdapter() == null && mMoviesView.getAdapter().getItemCount() == 0) {
                    mMoviesView.setVisibility(View.GONE);
                    mNodataView.setVisibility(View.VISIBLE);
                } else {
                    mNodataView.setVisibility(View.GONE);
                    mMoviesView.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        // bind UI
        mRefreshButton.setOnClickListener(this);

        if (TextUtils.isEmpty(mMovieService.getApiKey())) {
            String apiKey = getSharedPreferences(MovieApplication.PREF_NAME, MODE_PRIVATE).getString(MovieApplication.PREF_APIKEY, "");
            if (TextUtils.isEmpty(apiKey)) {
                startActivityForResult(SettingsActivity.newIntent(this), REQCODE_SETTINGS);
            } else {
                mMovieService.setApiKey(apiKey);
            }
        }
        loadMovies(false);
    }

    @Override
    protected void onStop() {
        super.onStop();
        // unbind UI
        mRefreshButton.setOnClickListener(null);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQCODE_SETTINGS && resultCode == RESULT_OK) {
            String newApiKey = data.getStringExtra(SettingsActivity.EXTRA_APIKEY);
            String apiKey = getSharedPreferences(MovieApplication.PREF_NAME, MODE_PRIVATE).getString(MovieApplication.PREF_APIKEY, "");
            if (!TextUtils.equals(apiKey, newApiKey)) {
                getSharedPreferences(MovieApplication.PREF_NAME, MODE_PRIVATE)
                        .edit()
                        .putString(MovieApplication.PREF_APIKEY, newApiKey)
                        .commit();
                mMovieService.setApiKey(newApiKey);
                loadMovies(true);
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        MenuItem itemMostPopular = menu.findItem(R.id.action_sortby_mostpopular);
        MenuItem itemTopRated = menu.findItem(R.id.action_sortby_rate);

        for (MenuItem item : new MenuItem[]{itemMostPopular, itemTopRated}) {
            String title = item.getTitle().toString();
            if (title.indexOf(">") >= 0) {
                title = title.substring(2, title.length());
                item.setTitle(title);
            }
        }
        switch (mCurrentMovieSort) {
            case MostPopular:
                itemMostPopular.setTitle(String.format("> %s", itemMostPopular.getTitle()));
                break;
            default:
                itemTopRated.setTitle(String.format("> %s", itemTopRated.getTitle()));
                break;
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_sortby_rate:
                mCurrentMovieSort = MoviesSortByEnum.TopRated;
                loadMovies(true);
                return true;
            case R.id.action_sortby_mostpopular:
                mCurrentMovieSort = MoviesSortByEnum.MostPopular;
                loadMovies(true);
                return true;
            case R.id.action_refresh:
                loadMovies(true);
                return true;
            case R.id.action_settings:
                String apiKey = getSharedPreferences(MovieApplication.PREF_NAME, MODE_PRIVATE).getString(MovieApplication.PREF_APIKEY, "");
                startActivityForResult(SettingsActivity.newIntent(this, apiKey), REQCODE_SETTINGS);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(EXTRA_LAST_SORTBY, String.valueOf(mCurrentMovieSort));
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        if (savedInstanceState != null) {
            if (savedInstanceState.containsKey(EXTRA_LAST_SORTBY)) {
                String value = savedInstanceState.getString(EXTRA_LAST_SORTBY);
                mCurrentMovieSort = MoviesSortByEnum.valueOf(value);
            }
        }
    }

    @Override
    public void onMovieClick(long id, Movie movie) {
        startActivity(MovieDetailsActivity.newIntent(this, movie));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case android.R.id.button1: {
                loadMovies(true);
                break;
            }
        }
    }
}
