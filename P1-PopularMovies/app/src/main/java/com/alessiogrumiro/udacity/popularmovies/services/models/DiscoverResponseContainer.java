package com.alessiogrumiro.udacity.popularmovies.services.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Alessio Grumiro on 02/02/17.
 */

public class DiscoverResponseContainer {

    @SerializedName("page")
    private int page;

    @SerializedName("total_results")
    private int totalResults;

    @SerializedName("total_pages")
    private int totalPages;

    @SerializedName("results")
    private List<MovieDb> results;

    public int getTotalResults() {
        return totalResults;
    }

    public void setTotalResults(int totalResults) {
        this.totalResults = totalResults;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public List<MovieDb> getResults() {
        return results;
    }

    public void setResults(List<MovieDb> results) {
        this.results = results;
    }
}
