package com.example.receiptas;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Path;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.preference.PreferenceManager;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.navigation.NavigationView;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Locale;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class MainActivity extends AppCompatActivity {

    private MainViewModel mainViewModel;
    private AppBarConfiguration mAppBarConfiguration;
    private DrawerLayout drawerLayout;
    private Toolbar toolbar;
    public int currentFragmentId;
    private SharedPreferences sharedPref;

    private boolean isTablet;

    private static final int PERMISSION_CODE = 101;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        this.sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        this.loadLanguagePreference();
        this.loadThemePreference();
        super.onCreate(savedInstanceState);

        this.writeFakeJson();

        this.mainViewModel = new ViewModelProvider(this).get(MainViewModel.class);
        setContentView(R.layout.activity_drawer);

        this.toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        this.drawerLayout = findViewById(R.id.drawer_layout);

        this.isTablet = getResources().getBoolean(R.bool.isTablet);

        if(this.isTablet()){
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
            this.lockDrawerOpen();
        } else {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }

        NavigationView navigationView = findViewById(R.id.nav_view);

        this.requestPermissions();

        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_history, R.id.nav_scan_receipt, R.id.nav_receive, R.id.nav_settings)
                .setDrawerLayout(this.drawerLayout)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        navController.addOnDestinationChangedListener(new NavController.OnDestinationChangedListener(){
            @Override
            public void onDestinationChanged(@NonNull NavController controller,
                                             @NonNull NavDestination destination, @Nullable Bundle arguments) {
                currentFragmentId = destination.getId();
                invalidateOptionsMenu();
            }
        });

        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        if(getIntent().getBooleanExtra("fromSettings", false)){
            navController.navigate(R.id.nav_settings);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if(this.isTablet() && (this.currentFragmentId == R.id.nav_history ||
                               this.currentFragmentId == R.id.nav_scan_receipt ||
                               this.currentFragmentId == R.id.nav_receive ||
                               this.currentFragmentId == R.id.nav_settings)){
            getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        } else {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        return false;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration) || super.onSupportNavigateUp();
    }

    public void requestPermissions(){
        boolean hasReadFilePermission = (ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED);
        boolean hasWriteFilePermission = (ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED);
        boolean hasCameraPermission = (ContextCompat.checkSelfPermission(this,
                Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED);
        boolean hasBtPermission = (ContextCompat.checkSelfPermission(this,
                Manifest.permission.BLUETOOTH) == PackageManager.PERMISSION_GRANTED);

        if(!hasReadFilePermission || !hasCameraPermission || !hasWriteFilePermission || !hasBtPermission){
            ActivityCompat.requestPermissions(this,
                    new String[]{
                            Manifest.permission.READ_EXTERNAL_STORAGE,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE,
                            Manifest.permission.CAMERA,
                            Manifest.permission.BLUETOOTH}, PERMISSION_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults){
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(requestCode == PERMISSION_CODE){
            if(grantResults[0] == PackageManager.PERMISSION_DENIED){
                new MaterialAlertDialogBuilder(this)
                        .setTitle(getString(R.string.request_permission_title))
                        .setMessage(getString(R.string.request_permission_message))
                        .setNeutralButton(R.string.request_permission_button_neutral, new DialogInterface.OnClickListener(){
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                finishAffinity();
                            }
                        })
                        .setPositiveButton(R.string.request_permission_button_accept, new DialogInterface.OnClickListener(){
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                requestPermissions();
                            }
                        })
                        .show();
            }
        }
    }

    public void hideKeyboard(){
        InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
        View view = getCurrentFocus();
        if(view != null){
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    public boolean isTablet(){
        return this.isTablet;
    }

    public void lockDrawerOpen(){
        this.drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_OPEN);
    }

    public void lockDrawerClosed(){
        this.drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
    }

    public void unlockDrawer(){
        this.drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
    }

    public Toolbar getToolbar() {
        return toolbar;
    }

    public void loadThemePreference(){
        String favoriteThemeDefault = getResources().getString(R.string.settings_favorite_theme_default);
        String favoriteTheme = this.sharedPref.getString(getString(R.string.settings_favorite_theme), favoriteThemeDefault);

        int nightModeFlags = getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;
        String[] themes = getResources().getStringArray(R.array.settings_themes);
        int nightMode;

        if(favoriteTheme.equals(themes[0])){
            nightMode = AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM;
        } else if(favoriteTheme.equals(themes[1]) &&
                nightModeFlags == Configuration.UI_MODE_NIGHT_YES){
            nightMode = AppCompatDelegate.MODE_NIGHT_NO;
        } else if(favoriteTheme.equals(themes[2]) &&
                nightModeFlags == Configuration.UI_MODE_NIGHT_NO){
            nightMode = AppCompatDelegate.MODE_NIGHT_YES;
        } else {
            return;
        }

        AppCompatDelegate.setDefaultNightMode(nightMode);
    }

    public void loadLanguagePreference(){
        String favoriteLanguageDefault = getResources().getString(R.string.settings_favorite_language_default);
        String favoriteLanguage = this.sharedPref.getString(getString(R.string.settings_favorite_language), favoriteLanguageDefault);
        Locale locale;

        String[] languages = getResources().getStringArray(R.array.settings_languages);

        if(favoriteLanguage.equals(languages[0])){
            locale = new Locale("en");
        } else if(favoriteLanguage.equals(languages[1])){
            locale = new Locale("fr");
        } else {
            return;
        }

        Configuration configuration = new Configuration();
        configuration.setLocale(locale);

        getBaseContext().getResources().updateConfiguration(configuration,
                getBaseContext().getResources().getDisplayMetrics());
    }

    private void writeFakeJson(){
        String json = "[\n" +
                "   {\n" +
                "      \"name\":\"Example\",\n" +
                "      \"date\":\"1616282781804\",\n" +
                "      \"currency\":\"CAD\",\n" +
                "      \"participants\":[\n" +
                "         {\n" +
                "            \"name\":\"Romain\",\n" +
                "            \"payer\":false\n" +
                "         },\n" +
                "         {\n" +
                "            \"name\":\"Aurélien\",\n" +
                "            \"payer\":true\n" +
                "         },\n" +
                "         {\n" +
                "            \"name\":\"Nelson\",\n" +
                "            \"payer\":false\n" +
                "         }\n" +
                "      ],\n" +
                "      \"items\":[\n" +
                "         {\n" +
                "            \"name\":\"Chocolat\",\n" +
                "            \"price\":12.83,\n" +
                "            \"participants\":[\n" +
                "               \"Romain\",\n" +
                "               \"Nelson\"\n" +
                "            ]\n" +
                "         },\n" +
                "         {\n" +
                "            \"name\":\"Vanille\",\n" +
                "            \"price\":7.00,\n" +
                "            \"participants\":[\n" +
                "               \"Romain\"\n" +
                "            ]\n" +
                "         },\n" +
                "         {\n" +
                "            \"name\":\"Fraise\",\n" +
                "            \"price\":9.99,\n" +
                "            \"participants\":[\n" +
                "               \"Romain\",\n" +
                "               \"Aurélien\",\n" +
                "               \"Nelson\"\n" +
                "            ]\n" +
                "         },\n" +
                "         {\n" +
                "            \"name\":\"Banana Marbré\",\n" +
                "            \"price\":35,\n" +
                "            \"participants\":[]\n" +
                "         }\n" +
                "      ]\n" +
                "   }\n" +
                "]";

        String path = getFilesDir() + "/receipts.json";

        File json_file = new File(path);
        if(!json_file.exists()){
            try {
                json_file.createNewFile();

                FileWriter fileWriter = new FileWriter(path);
                fileWriter.write(json);
                fileWriter.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}