package com.bignerdranch.android.moviebuff;

import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.text.method.ScrollingMovementMethod;
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

import org.json.JSONException;

import java.util.List;

/**
 * A fragment that shows the datails of a particular movie
 */
public class DetailFragment extends Fragment implements Bindable<Movie> {

    // Private Members
    private CollapsingToolbarLayout collapsingToolbarLayout;
    private ImageView movieBackdropImageView;
    private TextView movieOverviewTextView;
    private TextView moviePopularityTextView;
    private ImageView moviePosterImageView;
    private RatingBar movieRatingBar;
    private TextView movieReleaseDateTextView;
    private TextView movieReviewTextView;
    private TextView movieTaglineTextView;
    private Toolbar toolbar;
    private boolean uiUpdated = false;
    // Constants
    private static final String ARG_MOVIE = DetailFragment.class.getName() + ".argMovie";
    private static final String LOG_TAG = DetailFragment.class.getSimpleName();
    private static final int REQUEST_FETCH_REVIEWS = 0;
    private static final int REQUEST_FETCH_TAGLINE = 1;

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
    public void bind(final Movie movie) {
        if (movie == null || ! uiUpdated) return;

        String posterPath = MovieFetcher.getPosterUrl("w500", movie.getPosterPath());
        Picasso.with(getActivity()).load(posterPath).into(moviePosterImageView);

        String overview = movie.getOverview();
        if (TextUtils.isEmpty(overview)) {
            movieOverviewTextView.setVisibility(View.GONE);
            ((View) movieOverviewTextView.getParent()).setVisibility(View.GONE);
        }
        else
            movieOverviewTextView.setText(movie.getOverview());

        movieReleaseDateTextView.setText(movie.getReleaseDate());

        moviePopularityTextView.setText(String.valueOf((int) movie.getPopularity()));

        movieRatingBar.setRating((float) (movie.getVoteAverage() / 2));

        collapsingToolbarLayout.setTitle(movie.getOriginalTitle());

        String backdropPath = MovieFetcher.getPosterUrl("w500", movie.getBackdropPath());
        Picasso.with(getActivity()).load(backdropPath).into(movieBackdropImageView);

        new MovieFetcher(new MovieFetcher.MovieFetcherListener() {

            // Overridden Methods
            @Override
            public void onComplete(String movieData, int requestCode) {
                try {
                    List<String> reviews = MovieFetcher.getReviews(movieData);
                    if (reviews == null | reviews.size() != 0)
                        movieReviewTextView.setText(reviews.get(0));
                    else {
                        movieReviewTextView.setVisibility(View.GONE);
                        ((View) movieReviewTextView.getParent()).setVisibility(View.GONE);
                    }
                } catch (JSONException e) {
                    Log.e(LOG_TAG, "Couldn't fetch reviews", e);
                    e.printStackTrace();
                }
            }
        }, REQUEST_FETCH_REVIEWS).execute(String.valueOf(movie.getId()) + "/reviews");

        new MovieFetcher(new MovieFetcher.MovieFetcherListener() {

            // Overridden Methods
            @Override
            public void onComplete(String movieData, int requestCode) {
                try {
                    movieTaglineTextView.setText(MovieFetcher.getTagline(movieData));
                } catch (JSONException e) {
                    Log.e(LOG_TAG, "Couldn't fetch tagline", e);
                    e.printStackTrace();
                }
            }
        }, REQUEST_FETCH_TAGLINE).execute(String.valueOf(movie.getId()));

    }

    // Private Methods
    private void updateUi(View view) {
        moviePosterImageView = (ImageView) view.findViewById(R.id.fragment_detail_movie_poster_image_view);
        movieTaglineTextView = (TextView) view.findViewById(R.id.fragment_detail_movie_tagline_text_view);
        movieOverviewTextView = (TextView) view.findViewById(R.id.fragment_detail_movie_overview_text_view);
        movieReleaseDateTextView = (TextView) view.findViewById(R.id.fragment_detail_movie_release_date_text_view);
        moviePopularityTextView = (TextView) view.findViewById(R.id.fragment_detail_movie_popularity_text_view);
        movieRatingBar = (RatingBar) view.findViewById(R.id.fragment_detail_movie_rating_bar);
        toolbar = (Toolbar) view.findViewById(R.id.fragment_detail_toolbar);
        collapsingToolbarLayout = (CollapsingToolbarLayout) view.findViewById(R.id.fragment_detail_collapsing_toolbar_layout);
        movieBackdropImageView = (ImageView) view.findViewById(R.id.fragment_detail_collapsing_toolbar_backdrop_image_view);
        movieReviewTextView = (TextView) view.findViewById(R.id.fragment_detail_movie_review_text_view);

        movieOverviewTextView.setMovementMethod(new ScrollingMovementMethod());

        AppCompatActivity activity = (AppCompatActivity) getActivity();
        activity.setSupportActionBar(toolbar);

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
