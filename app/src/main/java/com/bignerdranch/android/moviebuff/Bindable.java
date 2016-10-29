package com.bignerdranch.android.moviebuff;

/**
 * An interface which knows how to bind objects of type @param<T>
 */
interface Bindable<T> {

    // Public Methods
    void bind(T object);
}
