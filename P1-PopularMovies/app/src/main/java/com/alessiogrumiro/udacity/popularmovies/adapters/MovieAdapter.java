package com.alessiogrumiro.udacity.popularmovies.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.alessiogrumiro.udacity.popularmovies.R;
import com.alessiogrumiro.udacity.popularmovies.listeners.OnMovieClickListener;
import com.alessiogrumiro.udacity.popularmovies.models.Movie;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by Alessio Grumiro on 25/01/17.
 */

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieViewHolder> {

    private OnMovieClickListener mOnMovieListener;
    private List<Movie> mMovies;
    private Context mContext;

    public MovieAdapter(Context context, OnMovieClickListener listener) {
        mOnMovieListener = listener;
        mContext = context;
    }

    public void setData(List<Movie> movies) {
        mMovies = movies;
        notifyDataSetChanged();
    }

    @Override
    public MovieViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.listitem_movie, parent, false);
        return new MovieViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MovieViewHolder holder, int position) {
        if (mMovies != null && mMovies.size() > position)
        holder.bind(mMovies.get(position));
    }

    @Override
    public int getItemCount() {
        return (mMovies != null) ? mMovies.size() : 0;
    }

    @Override
    public long getItemId(int position) {
        return mMovies != null && mMovies.size() > position ? mMovies.get(position).getId() : -1;
    }

    class MovieViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private static final String TAG = "MovieViewHolder";

        ImageView mPosterView;

        public MovieViewHolder(View view) {
            super(view);
            mPosterView = (ImageView) view.findViewById(android.R.id.icon);
            view.setOnClickListener(this);
        }


        public void bind(Movie movie) {
            if (movie != null)
            Picasso.with(mContext)
                    .load(movie.getPosterUrl())
                    .placeholder(mContext.getDrawable(R.mipmap.placeholder))
                    .into(mPosterView);
        }

        @Override
        public void onClick(View v) {
            if (mOnMovieListener != null)
                mOnMovieListener.onMovieClick(getItemId(), mMovies.get(getAdapterPosition()));

        }
    }
}
