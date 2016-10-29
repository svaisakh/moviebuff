package com.bignerdranch.android.moviebuff;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bignerdranch.android.moviebuff.Model.Movie;
import com.bignerdranch.android.moviebuff.Network.MovieFetcher;
import com.squareup.picasso.Picasso;

/**
 * A fragment that shows the datails of a particular movie
 */
public class DetailFragment extends Fragment implements Bindable<Movie> {

    // Private Members
    private TextView movieOverviewTextView;
    private TextView moviePopularityTextView;
    private ImageView moviePosterImageView;
    private RatingBar movieRatingBar;
    private TextView movieReleaseDateTextView;
    private TextView movieTitleTextView;
    private boolean uiUpdated = false;
    // Constants
    private static final String ARG_MOVIE = DetailFragment.class.getName() + ".argMovie";

    public DetailFragment() {
        // Required empty public constructor
    }

    // Overridden Methods
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_detail, container, false);

        updateUi(view);

        bind((Movie) getArguments().getSerializable(ARG_MOVIE));

        return view;
    }

    @Override
    public void bind(Movie movie) {
        if (movie == null || ! uiUpdated) return;

        String posterPath = MovieFetcher.getPosterUrl("w342", movie.getPosterPath());
        Picasso.with(getActivity()).load(posterPath).into(moviePosterImageView);

        movieTitleTextView.setText(movie.getOriginalTitle());

        movieOverviewTextView.setText(movie.getOverview());

        movieReleaseDateTextView.setText(movie.getReleaseDate());

        moviePopularityTextView.setText(String.valueOf(movie.getPopularity()));

        movieRatingBar.setRating((float) (movie.getVoteAverage() / 2));
    }

    // Private Methods
    private void updateUi(View view) {
        moviePosterImageView = (ImageView) view.findViewById(R.id.fragment_detail_movie_poster_image_view);
        movieTitleTextView = (TextView) view.findViewById(R.id.fragment_detail_movie_title_text_view);
        movieOverviewTextView = (TextView) view.findViewById(R.id.fragment_detail_movie_overview_text_view);
        movieReleaseDateTextView = (TextView) view.findViewById(R.id.fragment_detail_movie_release_date_text_view);
        moviePopularityTextView = (TextView) view.findViewById(R.id.fragment_detail_movie_popularity_text_view);
        movieRatingBar = (RatingBar) view.findViewById(R.id.fragment_detail_movie_rating_bar);

        movieOverviewTextView.setMovementMethod(new ScrollingMovementMethod());
        uiUpdated = true;
    }

    // Package Private Methods
    static DetailFragment newInstance(Movie movie) {
        Bundle args = new Bundle();
        args.putSerializable(ARG_MOVIE, movie);
        DetailFragment fragment = new DetailFragment();
        fragment.setArguments(args);
        return fragment;
    }
}
