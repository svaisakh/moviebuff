package com.bignerdranch.android.moviebuff.Model;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

/**
 * Created by Vaisakh on 27-10-2016.
 * Holds all information about the movie gathered from the database
 */

public class Movie implements Serializable {

    // Private Members
    private String backdropPath;
    private long id;
    private String jsonBluePrint;
    private String originalTitle;
    private String overview;
    private double popularity;
    private String posterPath;
    private String releaseDate;
    private double voteAverage;

    // Constructor(s)
    public Movie(JSONObject jsonMovie) throws JSONException {
        jsonBluePrint = jsonMovie.toString();
    }

    // Getter Methods
    public String getBackdropPath() {
        return backdropPath;
    }

    public long getId() {
        return id;
    }

    private String getJsonBluePrint() {
        return jsonBluePrint;
    }

    public String getOriginalTitle() {
        return originalTitle;
    }

    public String getOverview() {
        return overview;
    }

    public double getPopularity() {
        return popularity;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public double getVoteAverage() {
        return voteAverage;
    }

    // Setter Methods
    public void setBackdropPath(String backdropPath) {
        this.backdropPath = backdropPath;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setOriginalTitle(String originalTitle) {
        this.originalTitle = originalTitle;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public void setPopularity(double popularity) {
        this.popularity = popularity;
    }

    public void setPosterPath(String posterPath) {
        this.posterPath = posterPath;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public void setVoteAverage(double voteAverage) {
        this.voteAverage = voteAverage;
    }

    // Private Methods

}
