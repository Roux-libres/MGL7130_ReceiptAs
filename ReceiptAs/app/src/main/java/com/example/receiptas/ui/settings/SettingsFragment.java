package com.example.receiptas.ui.settings;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;

import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceManager;

import com.example.receiptas.MainActivity;
import com.example.receiptas.R;

public class SettingsFragment extends PreferenceFragmentCompat {

    private SharedPreferences.OnSharedPreferenceChangeListener preferenceListener;
    private SharedPreferences sharedPreferences;

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.settings, rootKey);

        try {
            String version = getContext().getPackageManager().getPackageInfo(getContext().getPackageName(), 0).versionName;
            findPreference(getString(R.string.settings_version)).setSummary(version);
        } catch (PackageManager.NameNotFoundException e) {
            findPreference(getString(R.string.settings_version)).setVisible(false);
            e.printStackTrace();
        }

        this.sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        this.preferenceListener = new SharedPreferences.OnSharedPreferenceChangeListener() {
            public void onSharedPreferenceChanged(SharedPreferences prefs, String key) {
                if(key.equals(getString(R.string.settings_favorite_theme))){
                    //TODO: check if the restart of the activity is really necessary
                    //((MainActivity) getActivity()).loadThemePreference();
                    Intent intent = getActivity().getIntent();
                    intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    getActivity().finish();
                    startActivity(intent);
                }
            };
        };

        this.sharedPreferences.registerOnSharedPreferenceChangeListener(preferenceListener);
    }
}