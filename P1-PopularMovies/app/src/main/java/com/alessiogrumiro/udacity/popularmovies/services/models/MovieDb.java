package com.alessiogrumiro.udacity.popularmovies.services.models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Alessio Grumiro on 27/01/17.
 */

public class MovieDb {

    @SerializedName("id")
    private long id;

    @SerializedName("title")
    private String title;

    @SerializedName("poster_path")
    private String posterUrl;

    @SerializedName("release_date")
    private String releaseDate;

    @SerializedName("vote_count")
    private int voteCount;

    @SerializedName("vote_average")
    private float voteAverage;

    @SerializedName("overview")
    private String overview;

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public int getVoteCount() {
        return voteCount;
    }

    public void setVoteCount(int voteCount) {
        this.voteCount = voteCount;
    }

    public float getVoteAverage() {
        return voteAverage;
    }

    public void setVoteAverage(float voteAverage) {
        this.voteAverage = voteAverage;
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
}
