package com.bignerdranch.android.moviebuff;

import android.support.v4.app.Fragment;

public class GalleryActivity extends SingleFragmentActivity {

    // Overridden Methods
    @Override
    protected Fragment newFragment() {
        return GalleryFragment.newInstance();
    }

}
