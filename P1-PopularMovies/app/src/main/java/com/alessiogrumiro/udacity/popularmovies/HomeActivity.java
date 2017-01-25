package com.alessiogrumiro.udacity.popularmovies;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

public class HomeActivity extends AppCompatActivity {

    private RecyclerView mMoviesView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        // binding UI
        mMoviesView = (RecyclerView)findViewById(R.id.rv_movies);

        // init recyclerview
        GridLayoutManager layoutManager = new GridLayoutManager(this, GridLayoutManager.DEFAULT_SPAN_COUNT);
        mMoviesView.setLayoutManager(layoutManager);

        mMoviesView.setHasFixedSize(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // TODO handles menu
        Toast.makeText(this, "Please handle " + item.getItemId(), Toast.LENGTH_SHORT).show();
        return super.onOptionsItemSelected(item);
    }
}
