package com.bignerdranch.android.moviebuff.Network;

import android.net.Uri;
import android.os.AsyncTask;
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

    // Constants
    /**
     * My v3 API Key for themoviedb.org
     */
    private static final String API_KEY = "b6e556a111d2a27b4ff4144b5f4911ef";
    /**
     * The base uri for the data. Note the /3/ added for v3 API Key
     */
    private static final String BASE_URI = "https://api.themoviedb.org/3/movie";
    public static final String THUMBNAIL_BASE_URI = "http://image.tmdb.org/t/p/w342";

    // Constructor(s)

    /**
     * Assigns the list of movies to be fetched into
     */
    public MovieFetcher(MovieFetcherListener fetcherListener) {
        this.fetcherListener = fetcherListener;
    }

    // Private Methods

    /**
     * Inflates all Movies from this JSONArray
     *
     * @param jsonArray Array of JSONObjects representing a Movie
     */
    private static List<Movie> inflate(JSONArray jsonArray) throws JSONException {
        List<Movie> movieList = new ArrayList<>();
        JSONObject movieJSONObject;
        Movie movie;
        for (int i = 0; i < jsonArray.length(); i++) {
            movieJSONObject = jsonArray.getJSONObject(i);
            movie = new Movie(movieJSONObject);
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
    private static JSONArray parse(String jsonString) throws JSONException {
        final String OWM_RESULTS = "results";

        JSONObject nodeJSONObject = new JSONObject(jsonString);

        return nodeJSONObject.getJSONArray(OWM_RESULTS);
    }

    // Public Methods

    /**
     * Fetches a list of movies from themoviedb.org customized according to the appended URL
     *
     * @param appendString the URL that gets appended to the base URL (excluding API key
     * @param page         the page that needs to be queried from
     */
    public void fetch(String appendString, int page) {
        new FetchMovieTask().execute(appendString, String.valueOf(page));
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
            List<Movie> movies = null;
            try {
                movies = inflate(parse(movieData));
            } catch (JSONException e) {
                Log.e(LOG_TAG, "Trouble brewing!", e);
                e.printStackTrace();
            }
            fetcherListener.onMovieLoad(movies);
        }

        /**
         * Fetches movie JSON string from themoviedb.org
         *
         * @param params The rest of the path of the query.
         *               Index 0 should be the movie path that is needed (eg. popular/top_rated etc.)
         *               Index 1 is the page number
         * @return Returns the string obtained from the query
         */
        @Override
        protected String doInBackground(String... params) {

            if (params.length == 0) return null;

            Uri movieUri = Uri.parse(BASE_URI)
                              .buildUpon()
                              .appendPath(params[0])
                              .appendQueryParameter("page", params[1])
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

    /**
     * Listener for events regarding MovieFetcher
     * Created by Vaisakh on 28-10-2016.
     */
    public interface MovieFetcherListener {

        // Public Methods

        /**
         * Callback triggered after movie list is loaded
         */
        void onMovieLoad(List<Movie> movies);

    }

}
