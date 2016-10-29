package com.bignerdranch.android.moviebuff;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bignerdranch.android.moviebuff.Model.Movie;
import com.bignerdranch.android.moviebuff.Network.MovieFetcher;
import com.squareup.picasso.Picasso;

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
    // Constants
    private static final String LOG_TAG = GalleryFragment.class.getSimpleName();

    // Constructor(s)

    public GalleryFragment() {
        // Required empty public constructor
    }

    // Overridden Methods
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_gallery, container, false);

        movies = new ArrayList<>();

        updateUi(view);

        String path = "popular";
        fetchData();

        return view;
    }

    // Private Methods

    /**
     * Queries the API for movies
     */
    private void fetchData() {
        if (fetcher == null) fetcher = new MovieFetcher(new MovieFetcher.MovieFetcherListener() {

            // Overridden Methods
            @Override
            public void onMovieLoad(List<Movie> movies) {
                GalleryFragment.this.movies.addAll(movies);
                movieRecyclerView.getAdapter().notifyDataSetChanged();
            }
        });
        fetcher.fetch(path, ++ page);
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

        abstract class BindableMovieHolder extends RecyclerView.ViewHolder implements Binder<Movie> {

            BindableMovieHolder(View itemView) {
                super(itemView);
            }
        }

        class LargeMovieHolder extends BindableMovieHolder implements Binder<Movie> {

            // Private Members

            private final String LOG_TAG = LargeMovieHolder.class.getSimpleName();
            private TextView movieOverviewTextView;
            private ImageView movieThumbnailImageView;
            private TextView movieTitleTextView;
            private RatingBar moviewRatingBar;

            // Constants

            LargeMovieHolder(View itemView) {
                super(itemView);
                movieThumbnailImageView = (ImageView) itemView.findViewById(R.id.item_movie_large_image_view);
                movieTitleTextView = (TextView) itemView.findViewById(R.id.item_movie_large_title_text_view);
                movieOverviewTextView = (TextView) itemView.findViewById(R.id.item_movie_large_overview_text_view);
                moviewRatingBar = (RatingBar) itemView.findViewById(R.id.item_movie_large_rating_bar_view);
            }

            // Overridden Methods
            @Override
            public void bind(Movie movie) {
                if (movies.indexOf(movie) == movies.size() - 1) fetchData();

                String posterPath = MovieFetcher.getPosterUrl("w342", movie.getPosterPath());
                Picasso.with(getActivity()).load(posterPath).into(movieThumbnailImageView);

                movieTitleTextView.setText(movie.getOriginalTitle());

                String shortOverview = movie.getOverview().substring(0, 50);
                movieOverviewTextView.setText(shortOverview.substring(0, shortOverview.lastIndexOf(" ")));

                moviewRatingBar.setRating((float) movie.getVoteAverage() / 2);
            }
        }

        class SmallMovieHolder extends BindableMovieHolder implements Binder<Movie> {

            // Private Members

            private final String LOG_TAG = SmallMovieHolder.class.getSimpleName();
            private ImageView movieThumbnailImageView;

            // Constants

            SmallMovieHolder(View itemView) {
                super(itemView);
                movieThumbnailImageView = (ImageView) itemView.findViewById(R.id.item_movie_small_image_view);
            }

            // Overridden Methods
            @Override
            public void bind(Movie movie) {
                if (movies.indexOf(movie) == movies.size() - 1) fetchData();

                String posterPath = MovieFetcher.getPosterUrl("w500", movie.getPosterPath());
                Picasso.with(getActivity()).load(posterPath).into(movieThumbnailImageView);
            }
        }

    }

    /**
     * An interface which knows how to bind objects of type @param <T>
     */
    interface Binder<T> {

        // Public Methods
        void bind(T object);
    }

}
