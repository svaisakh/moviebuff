package com.bignerdranch.android.moviebuff;

import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bignerdranch.android.moviebuff.Model.Movie;
import com.bignerdranch.android.moviebuff.Network.MovieFetcher;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class GalleryFragment extends Fragment {

    // Private Members
    private MovieFetcher fetcher;
    private RecyclerView movieRecyclerView;
    private List<Movie> movies;
    private int page = 0;
    /**
     * The path to append to the query (excluding API key)
     */
    private String path = "popular";
    private ImageButton settingsImageButton;
    // Constants
    private static final String LOG_TAG = GalleryFragment.class.getSimpleName();
    private static final String PATH_POPULAR = "popular";
    private static final String PATH_TOP_RATED = "top_rated";
    private static final int REQUEST_FETCH_MOVIE_LIST = 0;

    // Constructor(s)

    public GalleryFragment() {
        // Required empty public constructor
    }

    // Overridden Methods
    @Override
    public void onResume() {
        super.onResume();
        checkMovieType();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_fragment_gallery, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_fragment_gallery_settings:
                startActivity(SettingsActivity.starterIntent(getActivity()));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_gallery, container, false);

        setHasOptionsMenu(true);

        movies = new ArrayList<>();

        updateUi(view);

        if (PreferenceManager.getDefaultSharedPreferences(getActivity()).getString(getString(R.string.movies_type_preference_key), getString(R.string.movies_type_popular)).equals(getString(R.string.movies_type_top_rated)))
            path = PATH_TOP_RATED;
        fetchData();

        return view;
    }

    // Private Methods
    private void checkMovieType() {
        String movieType = PreferenceManager.getDefaultSharedPreferences(getActivity()).getString(getString(R.string.movies_type_preference_key), getString(R.string.movies_type_popular));
        if (movieType.equals(getString(R.string.movies_type_popular))) {
            if (! path.equals(PATH_POPULAR)) {
                path = PATH_POPULAR;
                page = 0;
                movies.clear();
                fetchData();
            }
        }
        if (movieType.equals(getString(R.string.movies_type_top_rated))) {
            if (! path.equals(PATH_TOP_RATED)) {
                path = PATH_TOP_RATED;
                page = 0;
                movies.clear();
                fetchData();
            }
        }
    }

    /**
     * Queries the API for movies
     */
    private void fetchData() {
        if (fetcher == null) fetcher = new MovieFetcher(new MovieFetcher.MovieFetcherListener() {

            // Overridden Methods
            @Override
            public void onComplete(String movieData, int requestCode) {
                try {
                    JSONArray moviesJSONArray = MovieFetcher.parseMovieList(movieData);
                    if (moviesJSONArray == null) return;
                    List<Movie> movieList = MovieFetcher.inflate(moviesJSONArray);
                    if (movieList == null | movieList.size() == 0) return;
                    movies.addAll(movieList);
                } catch (JSONException e) {
                    Log.e(LOG_TAG, "Couldn't parse json data. Something wrong.", e);
                    e.printStackTrace();
                }
                movieRecyclerView.getAdapter().notifyDataSetChanged();
            }
        }, REQUEST_FETCH_MOVIE_LIST);
        fetcher.execute(path, "page", String.valueOf(++ page));
    }

    private void updateUi(View view) {
        if (movieRecyclerView == null) {
            movieRecyclerView = (RecyclerView) view.findViewById(R.id.fragment_gallery_recycler_view);

            if (movieRecyclerView.getAdapter() == null) {
                movieRecyclerView.setAdapter(new MovieAdapter(movies));
            }

            GridLayoutManager layoutManager = new GridLayoutManager(getActivity(), 2);
            layoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {

                // Overridden Methods
                @Override
                public int getSpanSize(int position) {
                    if (position % 5 == 0) return 2;
                    return 1;
                }
            });
            movieRecyclerView.setLayoutManager(layoutManager);
        }

        AppCompatActivity activity = (AppCompatActivity) getActivity();
        activity.setSupportActionBar((Toolbar) view.findViewById(R.id.fragment_gallery_toolbar));
    }

    // Package Private Methods

    static Fragment newInstance() {
        return new GalleryFragment();
    }

    // Inner Classes

    /**
     * Created by Vaisakh on 29-10-2016.
     */
    private class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.BindableMovieHolder> {

        // Private Members

        private final int VIEW_HOLDER_TYPE_LARGE = 1;
        /**
         * These values indicate which ViewHolder type to use
         */
        private final int VIEW_HOLDER_TYPE_SMALL = 0;
        private List<Movie> movies;

        // Constructor(s)

        MovieAdapter(List<Movie> movies) {
            this.movies = movies;
        }

        // Overridden Methods
        @Override
        public int getItemViewType(int position) {
            if (position % 5 == 0) return VIEW_HOLDER_TYPE_LARGE;
            else return VIEW_HOLDER_TYPE_SMALL;
        }

        @Override
        public BindableMovieHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView;
            switch (viewType) {
                case VIEW_HOLDER_TYPE_SMALL:
                    itemView = LayoutInflater.from(getActivity()).inflate(R.layout.item_movie_small_view, parent, false);
                    return new SmallMovieHolder(itemView);
                case VIEW_HOLDER_TYPE_LARGE:
                    itemView = LayoutInflater.from(getActivity()).inflate(R.layout.item_movie_large_view, parent, false);
                    return new LargeMovieHolder(itemView);
                default:
                    Log.e(LOG_TAG, "View holder has wrong type onCreateViewHolder()");
                    return null;
            }
        }

        @Override
        public void onBindViewHolder(BindableMovieHolder holder, int position) {
            holder.bind(movies.get(position));
        }

        @Override
        public int getItemCount() {
            return movies.size();
        }

        // Getter Methods

        private List<Movie> getMovies() {
            return movies;
        }

        // Setter Methods

        private void setMovies(List<Movie> movies) {
            this.movies = movies;
        }

        // Inner Classes

        abstract class BindableMovieHolder extends RecyclerView.ViewHolder implements Bindable<Movie>, View.OnClickListener {

            BindableMovieHolder(View itemView) {
                super(itemView);
            }
        }

        class LargeMovieHolder extends BindableMovieHolder {

            // Private Members
            private final String LOG_TAG = LargeMovieHolder.class.getSimpleName();
            private Movie movie;
            private TextView movieOverviewTextView;
            private ImageView movieThumbnailImageView;
            private TextView movieTitleTextView;
            private RatingBar moviewRatingBar;

            // Constants
            LargeMovieHolder(View itemView) {
                super(itemView);
                itemView.setOnClickListener(this);
                itemView.setVisibility(View.GONE);
                movieThumbnailImageView = (ImageView) itemView.findViewById(R.id.item_movie_large_image_view);
                movieTitleTextView = (TextView) itemView.findViewById(R.id.item_movie_large_title_text_view);
                movieOverviewTextView = (TextView) itemView.findViewById(R.id.item_movie_large_overview_text_view);
                moviewRatingBar = (RatingBar) itemView.findViewById(R.id.item_movie_large_rating_bar_view);
            }

            // Overridden Methods
            @Override
            public void onClick(View v) {
                startActivity(DetailActivity.starterIntent(movie, getActivity()));
            }

            @Override
            public void bind(Movie movie) {
                this.movie = movie;

                if (movies.indexOf(movie) == movies.size() - 1) fetchData();

                String posterPath = MovieFetcher.getPosterUrl("w342", movie.getPosterPath());
                new Picasso.Builder(getActivity()).listener(new Picasso.Listener() {

                    // Overridden Methods
                    @Override
                    public void onImageLoadFailed(Picasso picasso, Uri uri, Exception exception) {
                        itemView.setVisibility(View.GONE);
                    }
                }).build().with(getActivity()).load(posterPath).into(movieThumbnailImageView);
                itemView.setVisibility(View.VISIBLE);
                movieTitleTextView.setText(movie.getOriginalTitle());

                String shortOverview = movie.getOverview().substring(0, 50);
                movieOverviewTextView.setText(shortOverview.substring(0, shortOverview.lastIndexOf(" ")));

                moviewRatingBar.setRating((float) movie.getVoteAverage() / 2);
            }
        }

        class SmallMovieHolder extends BindableMovieHolder {

            // Private Members
            private final String LOG_TAG = SmallMovieHolder.class.getSimpleName();
            private Movie movie;
            private ImageView movieThumbnailImageView;

            // Constants
            SmallMovieHolder(View itemView) {
                super(itemView);
                itemView.setVisibility(View.GONE);
                itemView.setOnClickListener(this);
                movieThumbnailImageView = (ImageView) itemView.findViewById(R.id.item_movie_small_image_view);
            }

            // Overridden Methods
            @Override
            public void onClick(View v) {
                startActivity(DetailActivity.starterIntent(movie, getActivity()));
            }

            @Override
            public void bind(Movie movie) {
                this.movie = movie;
                if (movies.indexOf(movie) == movies.size() - 1) fetchData();

                String posterPath = MovieFetcher.getPosterUrl("w500", movie.getPosterPath());
                new Picasso.Builder(getActivity()).listener(new Picasso.Listener() {

                    // Overridden Methods
                    @Override
                    public void onImageLoadFailed(Picasso picasso, Uri uri, Exception exception) {
                        itemView.setVisibility(View.GONE);
                    }
                }).build().with(getActivity()).load(posterPath).into(movieThumbnailImageView);
                itemView.setVisibility(View.VISIBLE);
            }
        }

    }

}
