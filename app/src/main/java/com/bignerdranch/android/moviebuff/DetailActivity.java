package com.bignerdranch.android.moviebuff;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;

import com.bignerdranch.android.moviebuff.Model.Movie;

public class DetailActivity extends SingleFragmentActivity {

    // Private Members
    private Movie movie;
    // Constants
    private static final String EXTRA_MOVIE = DetailActivity.class.getName() + ".extraMovie";
    private static final String LOG_TAG = DetailActivity.class.getSimpleName();

    // Overridden Methods
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Bundle extras = getIntent().getExtras();
        if (extras == null) {
            Log.e(LOG_TAG, "Please start activity using the intent provided by starterIntent() method");
            finish();
        }
        else {
            Movie movieExtra = (Movie) getIntent().getExtras().getSerializable(EXTRA_MOVIE);
            if (movieExtra == null) {
                Log.e(LOG_TAG, "Movie is null");
                finish();
            }
            else {
                movie = movieExtra;
            }
        }
        super.onCreate(savedInstanceState);
    }

    @Override
    protected Fragment newFragment() {
        return DetailFragment.newInstance(movie);
    }

    // Private Methods
// Package Private Methods
    static Intent starterIntent(Movie movie, Context context) {
        Intent intent = new Intent(context, DetailActivity.class);
        intent.putExtra(EXTRA_MOVIE, movie);
        return intent;
    }
}
