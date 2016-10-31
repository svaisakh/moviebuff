package com.bignerdranch.android.moviebuff.Network;

import android.net.Uri;
import android.os.AsyncTask;
import android.text.TextUtils;
import android.util.Log;

import com.bignerdranch.android.moviebuff.Model.Movie;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Fetches movies from themoviedb.org
 * Created by Vaisakh on 27-10-2016.
 */
public class MovieFetcher {

    // Private Members
    private MovieFetcherListener fetcherListener;
    private int requestCode;
    // Constants
    /**
     * My v3 API Key for themoviedb.org
     */
    private static final String API_KEY = "b6e556a111d2a27b4ff4144b5f4911ef";
    /**
     * The base uri for the data. Note the /3/ added for v3 API Key
     */
    private static final String BASE_URI = "https://api.themoviedb.org/3/movie";
    private static final String LOG_TAG = MovieFetcher.class.getSimpleName();
    private static final String THUMBNAIL_BASE_URI = "http://image.tmdb.org/t/p";

    // Constructor(s)

    /**
     * Assigns the list of movies to be fetched into
     */
    public MovieFetcher(MovieFetcherListener fetcherListener, int requestCode) {
        this.fetcherListener = fetcherListener;
        this.requestCode = requestCode;
    }


    // Private Methods

    /**
     * Inflates JSONObject to a new movie object
     *
     * @param movieJSONObject the JSONObject from which to inflate
     */
    private static Movie inflate(JSONObject movieJSONObject) throws JSONException {
        if (movieJSONObject == null) return null;

        Movie movie = new Movie(movieJSONObject);
        final String OWM_ID = "id";
        final String OWM_ORIGINAL_TITLE = "original_title";
        final String OWM_OVERVIEW = "overview";
        final String OWM_POPULARITY = "popularity";
        final String OWM_POSTER_PATH = "poster_path";
        final String OWM_RELEASE_DATE = "release_date";
        final String OWM_VOTE_AVERAGE = "vote_average";
        final String OWM_BACKDROP_PATH = "backdrop_path";

        long id = movieJSONObject.getLong(OWM_ID);
        String originalTitle = movieJSONObject.getString(OWM_ORIGINAL_TITLE);
        String overview = movieJSONObject.getString(OWM_OVERVIEW);
        double popularity = movieJSONObject.getDouble(OWM_POPULARITY);
        String posterPath = movieJSONObject.getString(OWM_POSTER_PATH);
        String releaseDate = movieJSONObject.getString(OWM_RELEASE_DATE);
        double voteAverage = movieJSONObject.getDouble(OWM_VOTE_AVERAGE);
        String backdropPath = movieJSONObject.getString(OWM_BACKDROP_PATH);

        movie.setId(id);
        movie.setOriginalTitle(originalTitle);
        movie.setOverview(overview);
        movie.setPopularity(popularity);
        movie.setPosterPath(posterPath);
        movie.setReleaseDate(releaseDate);
        movie.setVoteAverage(voteAverage);
        movie.setBackdropPath(backdropPath);

        return movie;
    }

    /**
     * Parses given JSON string and returns JSONArray of deflated Movies
     *
     * @param jsonString String containing results of query in JSON format
     * @return JSONArray containing movie data obtained from the given string
     */
    private static JSONObject parseMovie(String jsonString) throws JSONException {
        if (TextUtils.isEmpty(jsonString)) return null;

        return new JSONObject(jsonString);
    }

    // Public Methods
    public void execute(String... params) {
        new MovieFetcherTask().execute(params);
    }

    public static String getPosterUrl(String size, String moviePosterPath) {
        if (TextUtils.isEmpty(size) || TextUtils.isEmpty(moviePosterPath)) return null;

        return new StringBuilder(THUMBNAIL_BASE_URI).append("/").append(size).append("/").append(moviePosterPath).toString();
    }

    /**
     * Inflates all Movies from this JSONArray
     *
     * @param jsonArray Array of JSONObjects representing a Movie
     */
    public static List<Movie> inflate(JSONArray jsonArray) throws JSONException {
        if (jsonArray == null) return null;
        else if (jsonArray.length() == 0) return null;

        List<Movie> movieList = new ArrayList<>();
        JSONObject movieJSONObject;
        Movie movie;
        for (int i = 0; i < jsonArray.length(); i++) {
            movieJSONObject = jsonArray.getJSONObject(i);
            movie = inflate(movieJSONObject);
            movieList.add(movie);
        }
        return movieList;
    }

    /**
     * Parses given JSON string and returns JSONArray of deflated Movies
     *
     * @param jsonString String containing results of query in JSON format
     * @return JSONArray containing movie data obtained from the given string
     */
    public static JSONArray parseMovieList(String jsonString) throws JSONException {
        if (TextUtils.isEmpty(jsonString)) return null;

        final String OWM_RESULTS = "results";

        JSONObject nodeJSONObject = new JSONObject(jsonString);

        return nodeJSONObject.getJSONArray(OWM_RESULTS);
    }

    // Inner Classes
    private class MovieFetcherTask extends AsyncTask<String, Void, String> {

        // Overridden Methods

        /**
         * Parses the JSON data string and inflates into the movie list for the outer class
         *
         * @param movieData the data returned by doInBackground
         */
        @Override
        protected void onPostExecute(String movieData) {
            fetcherListener.onComplete(movieData, requestCode);
        }

        /**
         * Fetches movie JSON string from themoviedb.org
         *
         * @param params The rest of the path of the query.
         *               Index 0 should be the movie path that is needed (eg. popular/top_rated etc.)
         *               Other indices should be key-value pairs
         * @return Returns the string obtained from the query
         */
        @Override
        protected String doInBackground(String... params) {

            if (params.length == 0) return null;

            Uri.Builder movieUriBuilder = Uri.parse(BASE_URI)
                                             .buildUpon()
                                             .appendPath(params[0])
                                             .appendQueryParameter("api_key", API_KEY);

            for (int i = 1; i < params.length - 1; i += 2) {
                movieUriBuilder.appendQueryParameter(params[i], params[i + 1]);
            }

            Uri movieUri = movieUriBuilder.build();

            String movieData = null;
            HttpHelper helper = null;

            try {
                helper = new HttpHelper(movieUri);
                movieData = helper.toString();
            } catch (IOException e) {
                Log.e(LOG_TAG, "An error occured while connecting.", e);
                e.printStackTrace();
            } finally {
                if (helper != null) helper.disconnect();
            }

            return movieData;
        }

    }

    /**
     * Listener for events regarding MovieFetcher
     * Created by Vaisakh on 28-10-2016.
     */
    public interface MovieFetcherListener {

        // Public Methods

        /**
         * Callback triggered after movie list is loaded
         */
        void onComplete(String movieData, int requestCode);

    }

}
