package com.bignerdranch.android.moviebuff.Network;

/**
 * Listener for events regarding MovieFetcher
 * Created by Vaisakh on 28-10-2016.
 */

public interface MovieFetcherListener {

    // Public Methods

    /**
     * Callback triggered after movie list is loaded
     */
    void onMovieLoad();

}
