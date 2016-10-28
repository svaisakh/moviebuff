package com.bignerdranch.android.moviebuff.Network;

import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import com.bignerdranch.android.moviebuff.Model.Movie;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;

/**
 * Fetches movies from themoviedb.org
 * Created by Vaisakh on 27-10-2016.
 */
public class MovieFetcher {

    // Private Members

    private MovieFetcherListener fetcherListener;
    private List<Movie> movieList;

    // Constants

    /**
     * My v3 API Key for themoviedb.org
     */
    private static final String API_KEY = "b6e556a111d2a27b4ff4144b5f4911ef";
    /**
     * The base uri for the data. Note the /3/ added for v3 API Key
     */
    private static final String BASE_URI = "https://api.themoviedb.org/3/movie";

    // Constructor(s)

    /**
     * Assigns the list of movies to be fetched into
     */
    public MovieFetcher(List<Movie> movieList, MovieFetcherListener fetcherListener) {
        this.movieList = movieList;
        this.fetcherListener = fetcherListener;
    }

    // Private Methods

    /**
     * Inflates all Movies from this JSONArray
     *
     * @param jsonArray Array of JSONObjects representing a Movie
     */
    private void inflate(JSONArray jsonArray) throws JSONException {
        movieList.clear();
        JSONObject movieJSONObject;
        Movie movie;
        for (int i = 0; i < jsonArray.length(); i++) {
            movieJSONObject = jsonArray.getJSONObject(i);
            movie = new Movie(movieJSONObject);
            movieList.add(movie);
        }
    }

    /**
     * Parses given JSON string and returns JSONArray of deflated Movies
     *
     * @param jsonString String containing results of query in JSON format
     * @return JSONArray containing movie data obtained from the given string
     */
    private static JSONArray parse(String jsonString) throws JSONException {
        final String OWM_RESULTS = "results";

        JSONObject nodeJSONObject = new JSONObject(jsonString);

        return nodeJSONObject.getJSONArray(OWM_RESULTS);
    }

    // Public Methods

    /**
     * Fetches a list of movies from themoviedb.org customized according to the parameters
     *
     * @param category the category of movies (eg. popular/top_rated etc.)
     */
    public void fetch(String category) {
        new FetchMovieTask().execute(category);
    }

    // Inner Classes

    /**
     * Fetches the JSON string for the first parameter path (eg. execute("popular") gives popular movies).
     */
    private class FetchMovieTask extends AsyncTask<String, Void, String> {

        // Private Members
        private final String LOG_TAG = FetchMovieTask.class.getSimpleName();

        // Overridden Methods

        /**
         * Parses the JSON data string and inflates into the movie list for the outer class
         *
         * @param movieData the data returned by doInBackground
         */
        @Override
        protected void onPostExecute(String movieData) {
            try {
                inflate(parse(movieData));
            } catch (JSONException e) {
                Log.e(LOG_TAG, "Trouble brewing!", e);
                e.printStackTrace();
            }
            fetcherListener.onMovieLoad();
        }

        /**
         * Fetches movie JSON string from themoviedb.org
         *
         * @param params The movie path that is needed (eg. popular/top_rated etc.)
         *               Only the first string will be used.
         * @return Returns the string obtained from the query
         */
        @Override
        protected String doInBackground(String... params) {

            if (params.length == 0) return null;

            Uri movieUri = Uri.parse(BASE_URI)
                              .buildUpon()
                              .appendPath(params[0])
                              .appendQueryParameter("api_key", API_KEY)
                              .build();

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

}
