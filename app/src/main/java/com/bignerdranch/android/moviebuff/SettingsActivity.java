package com.bignerdranch.android.moviebuff;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;

public class SettingsActivity extends PreferenceActivity implements Preference.OnPreferenceChangeListener {

    // Overridden Methods
    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {
        preference.setSummary(newValue.toString());
        finish();
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.settings);

        bindPreferenceToValue(findPreference(getString(R.string.movies_type_preference_key)));
    }

    // Private Methods
    private void bindPreferenceToValue(Preference preference) {
        preference.setOnPreferenceChangeListener(this);

        preference.setSummary(PreferenceManager.getDefaultSharedPreferences(preference.getContext()).getString(getString(R.string.movies_type_preference_key), getString(R.string.movies_type_popular)));
    }

    // Package Private Methods
    static Intent starterIntent(Context context) {
        return new Intent(context, SettingsActivity.class);
    }

}
