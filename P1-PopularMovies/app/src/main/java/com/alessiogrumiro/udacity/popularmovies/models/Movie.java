package com.alessiogrumiro.udacity.popularmovies.models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Expected movie data to show in movie details screen
 * <p>
 * Created by Alessio Grumiro on 05/02/17.
 */

public class Movie implements Parcelable {

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public Movie createFromParcel(Parcel in) {
            return new Movie(in);
        }

        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };
    private long id;
    private String title;
    private String posterUrl;
    private String overview;
    private float voteAverage;

    public Movie() {
    }

    public Movie(Parcel in) {
        setId(in.readLong());
        setTitle(in.readString());
        setPosterUrl(in.readString());
        setOverview(in.readString());
        setVoteAverage(in.readFloat());
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPosterUrl() {
        return posterUrl;
    }

    public void setPosterUrl(String posterUrl) {
        this.posterUrl = posterUrl;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public float getVoteAverage() {
        return voteAverage;
    }

    public void setVoteAverage(float voteAverage) {
        this.voteAverage = voteAverage;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeString(title);
        dest.writeString(posterUrl);
        dest.writeString(overview);
        dest.writeFloat(voteAverage);
    }


}
