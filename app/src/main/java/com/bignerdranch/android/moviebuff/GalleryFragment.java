package com.bignerdranch.android.moviebuff;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class GalleryFragment extends Fragment {

    // Private Members
    private RecyclerView movieRecyclerView;

    public GalleryFragment() {
        // Required empty public constructor
    }

    // Overridden Methods
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_gallery, container, false);

        updateUi(view);

        return view;
    }

    // Private Methods
    private void updateUi(View view) {
        movieRecyclerView = (RecyclerView) view.findViewById(R.id.fragment_gallery_recycler_view);
        movieRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));
    }

    // Package Private Methods
    static Fragment newInstance() {
        GalleryFragment fragment = new GalleryFragment();
        return fragment;
    }

    // Inner Classes
    private class MovieAdapter extends RecyclerView.Adapter<MovieHolder> {

        // Private Members
        private List<Movie> movies;

        // Constructor(s)
        private MovieAdapter(List<Movie> movies) {
            this.movies = movies;
        }

        // Overridden Methods
        @Override
        public MovieHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(getActivity()).inflate(android.R.layout.simple_list_item_1, parent);//TODO: Update this to needed layout
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
    }

    private class MovieHolder extends RecyclerView.ViewHolder {

        // Private Members
        private Movie movieHeld;
        private TextView testTextView;//TODO: Delete this placeholder

        // Private Methods
        private void bindMovie(Movie movie) {
            movieHeld = movie;
            testTextView.setText(movie.getOriginalTitle());//TODO: Delete this placeholder
            //TODO: Bind movie details to views
        }

        MovieHolder(View itemView) {
            super(itemView);
            testTextView = (TextView) itemView;
        }

    }

}
