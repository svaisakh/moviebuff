package com.bignerdranch.android.moviebuff;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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
    private int page = 1;

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
        fetchData(path, page, true);

        return view;
    }

    // Private Methods

    /**
     * Queries the API for movies
     *
     * @param path the path to append to the query (excluding API key)
     */
    private void fetchData(String path, int page, final boolean append) {
        if (fetcher == null) fetcher = new MovieFetcher(new MovieFetcher.MovieFetcherListener() {

            // Overridden Methods
            @Override
            public void onMovieLoad(List<Movie> movies) {
                if (! append) GalleryFragment.this.movies.clear();
                GalleryFragment.this.movies.addAll(movies);
                movieRecyclerView.getAdapter().notifyDataSetChanged();
            }
        });
        fetcher.fetch(path, page);
    }

    private void updateUi(View view) {
        if (movieRecyclerView == null) {
            movieRecyclerView = (RecyclerView) view.findViewById(R.id.fragment_gallery_recycler_view);

            if (movieRecyclerView.getAdapter() == null) {
                movieRecyclerView.setAdapter(new MovieAdapter(movies));
            }

            movieRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

            movieRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {

                // Overridden Methods
                @Override
                public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                    if (dy <= 0) return;
                    if (! recyclerView.canScrollVertically(RecyclerView.VERTICAL)) {
                        String path = "popular";
                        fetchData(path, ++ page, true);
                    }
                }

            });
        }
    }

    // Package Private Methods

    static Fragment newInstance() {
        return new GalleryFragment();
    }

    // Inner Classes

    private class MovieAdapter extends RecyclerView.Adapter<MovieHolder> {

        // Private Members
        private List<Movie> movies;

        // Constructor(s)
        MovieAdapter(List<Movie> movies) {
            this.movies = movies;
        }

        // Overridden Methods
        @Override
        public MovieHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(getActivity()).inflate(R.layout.item_movie_thumbnail_view, parent, false);
            return new MovieHolder(itemView);
        }

        @Override
        public void onBindViewHolder(MovieHolder holder, int position) {
            holder.bindMovie(movies.get(position));
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
    }

    private class MovieHolder extends RecyclerView.ViewHolder {

        // Private Members

        private final String LOG_TAG = MovieHolder.class.getSimpleName();
        private TextView movieOverviewTextView;
        private ImageView movieThumbnailImageView;
        private TextView movieTitleTextView;
        private RatingBar moviewRatingBar;

        // Constants

        MovieHolder(View itemView) {
            super(itemView);
            movieThumbnailImageView = (ImageView) itemView.findViewById(R.id.item_movie_thumbnail_image_view);
            movieTitleTextView = (TextView) itemView.findViewById(R.id.item_movie_title_text_view);
            movieOverviewTextView = (TextView) itemView.findViewById(R.id.item_movie_overview_text_view);
            moviewRatingBar = (RatingBar) itemView.findViewById(R.id.item_movie_rating_bar_view);
        }

        // Private Methods

        private void bindMovie(Movie movie) {
            String posterPath = MovieFetcher.THUMBNAIL_BASE_URI + "/" + movie.getPosterPath();
            Picasso.with(getActivity()).load(posterPath).into(movieThumbnailImageView);

            movieTitleTextView.setText(movie.getOriginalTitle());

            String shortOverview = movie.getOverview().substring(0, 50);
            movieOverviewTextView.setText(shortOverview);

            moviewRatingBar.setRating((float) movie.getVoteAverage() / 2);

        }

    }

}
