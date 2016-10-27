package com.bignerdranch.android.moviebuff;

import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

abstract public class SingleFragmentActivity extends AppCompatActivity {

    // Overridden Methods
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_fragment);

        addFragment();
    }

    // Private Methods
    private void addFragment() {
        if (getSupportFragmentManager().findFragmentById(R.id.activity_single_fragment_frame) == null)
            getSupportFragmentManager().beginTransaction().add(R.id.activity_single_fragment_frame, newFragment()).commit();
    }

    // Protected Methods
    protected abstract Fragment newFragment();
}
