package com.bignerdranch.android.moviebuff;

import org.json.JSONObject;

import java.util.Date;

/**
 * Created by Vaisakh on 27-10-2016.
 * Holds all information about the movie gathered from the database
 */

class Movie {

    // Private Members
    private long id;
    private JSONObject jsonBluePrint;
    private String originalTitle;
    private String overview;
    private double popularity;
    private String poster_path;
    private Date releaseDate;
    private double voteAverage;

    // Constructor(s)
    private Movie(JSONObject jsonMovie) {
        jsonBluePrint = jsonMovie;
        //TODO: Inflate movie object from JSONObject
    }

    // Getter Methods
    private long getId() {
        return id;
    }

    String getOriginalTitle() {
        return originalTitle;
    }

    private String getOverview() {
        return overview;
    }

    private double getPopularity() {
        return popularity;
    }

    private String getPoster_path() {
        return poster_path;
    }

    private Date getReleaseDate() {
        return releaseDate;
    }

    private double getVoteAverage() {
        return voteAverage;
    }

}
