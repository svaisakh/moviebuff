package com.bignerdranch.android.moviebuff.Model;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Vaisakh on 27-10-2016.
 * Holds all information about the movie gathered from the database
 */

public class Movie {

    // Private Members
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
        inflate(jsonMovie);
    }

    // Getter Methods
    public long getId() {
        return id;
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

    private String getReleaseDate() {
        return releaseDate;
    }

    public double getVoteAverage() {
        return voteAverage;
    }

    // Setter Methods
    private void setId(long id) {
        this.id = id;
    }

    private void setOriginalTitle(String originalTitle) {
        this.originalTitle = originalTitle;
    }

    private void setOverview(String overview) {
        this.overview = overview;
    }

    private void setPopularity(double popularity) {
        this.popularity = popularity;
    }

    private void setPosterPath(String posterPath) {
        this.posterPath = posterPath;
    }

    private void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    private void setVoteAverage(double voteAverage) {
        this.voteAverage = voteAverage;
    }

    // Private Methods

    /**
     * Inflates JSONObject to a new movie object
     *
     * @param movieJSONObject the JSONObject from which to inflate
     */
    private void inflate(JSONObject movieJSONObject) throws JSONException {
        final String OWM_ID = "id";
        final String OWM_ORIGINAL_TITLE = "original_title";
        final String OWM_OVERVIEW = "overview";
        final String OWM_POPULARITY = "popularity";
        final String OWM_POSTER_PATH = "poster_path";
        final String OWM_RELEASE_DATE = "release_date";
        final String OWM_VOTE_AVERAGE = "vote_average";

        long id = movieJSONObject.getLong(OWM_ID);
        String originalTitle = movieJSONObject.getString(OWM_ORIGINAL_TITLE);
        String overview = movieJSONObject.getString(OWM_OVERVIEW);
        double popularity = movieJSONObject.getDouble(OWM_POPULARITY);
        String posterPath = movieJSONObject.getString(OWM_POSTER_PATH);
        String releaseDate = movieJSONObject.getString(OWM_RELEASE_DATE);
        double voteAverage = movieJSONObject.getDouble(OWM_VOTE_AVERAGE);

        setId(id);
        setOriginalTitle(originalTitle);
        setOverview(overview);
        setPopularity(popularity);
        setPosterPath(posterPath);
        setReleaseDate(releaseDate);
        setVoteAverage(voteAverage);
    }

}
