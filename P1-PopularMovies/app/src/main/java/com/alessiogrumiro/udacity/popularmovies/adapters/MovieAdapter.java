package com.alessiogrumiro.udacity.popularmovies.adapters;

import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.alessiogrumiro.udacity.popularmovies.listeners.OnMovieClickListener;

import java.util.List;

import info.movito.themoviedbapi.model.Artwork;
import info.movito.themoviedbapi.model.ArtworkType;
import info.movito.themoviedbapi.model.MovieDb;

/**
 * Created by Alessio Grumiro on 25/01/17.
 */

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieViewHolder> {

    private OnMovieClickListener mOnMovieListener;
    private List<MovieDb> mMovies;

    public MovieAdapter(List<MovieDb> movies, OnMovieClickListener listener) {
        mMovies = movies;
        mOnMovieListener = listener;
    }

    @Override
    public MovieViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(MovieViewHolder holder, int position) {
        holder.bind(mMovies.get(position));
    }

    @Override
    public int getItemCount() {
        return (mMovies != null) ? mMovies.size() : 0;
    }

    class MovieViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private static final String TAG = "MovieViewHolder";

        ImageView mPosterView;
        TextView mTitleView;

        public MovieViewHolder(View view) {
            super(view);
            mPosterView = (ImageView) view.findViewById(android.R.id.icon);
            mTitleView = (TextView) view.findViewById(android.R.id.title);
            view.setOnClickListener(this);
        }

        public void bind(MovieDb movie) {
            List<Artwork> artworks = movie.getImages(ArtworkType.POSTER);
            if (artworks != null && artworks.size() > 0) {
                try {
                    mPosterView.setImageURI(Uri.parse(artworks.get(0).getFilePath()));
                    // TODO handle image fail
                } catch (Exception e) {
                    Log.e(TAG, e.getMessage(), e);
                }
            }
            mTitleView.setText(movie.getTitle());
        }

        @Override
        public void onClick(View v) {
            if (mOnMovieListener != null)
                // TODO add movie id
                mOnMovieListener.onMovieClick(0);
        }
    }
}
