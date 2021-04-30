package com.example.receiptas;

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.navigation.NavigationView;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class MainActivity extends AppCompatActivity {

    private MainViewModel mainViewModel;
    private AppBarConfiguration mAppBarConfiguration;
    private DrawerLayout drawerLayout;
    private Toolbar toolbar;
    public int currentFragmentId;

    private boolean isTablet;

    private static final int PERMISSION_CODE = 101;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //this.writeFakeJson();

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

        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_history, R.id.nav_scan_receipt, R.id.nav_settings)
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
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if(this.isTablet() && (this.currentFragmentId == R.id.nav_history || this.currentFragmentId == R.id.nav_scan_receipt || this.currentFragmentId == R.id.nav_settings)){
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

        if(!hasReadFilePermission || !hasCameraPermission || !hasWriteFilePermission){
            ActivityCompat.requestPermissions(this,
                    new String[]{
                            Manifest.permission.READ_EXTERNAL_STORAGE,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE,
                            Manifest.permission.CAMERA}, PERMISSION_CODE);
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

    private void writeFakeJson(){
        String json = "[\n" +
                "   {\n" +
                "      \"name\":\"Glaces\",\n" +
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
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        try {
            FileWriter fileWriter = new FileWriter(path);
            fileWriter.write(json);
            fileWriter.close();
        } catch (IOException ioException){
            System.out.println(ioException);
        }
    }
}