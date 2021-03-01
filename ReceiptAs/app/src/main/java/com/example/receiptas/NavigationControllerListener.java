package com.example.receiptas;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;

public class NavigationControllerListener implements NavController.OnDestinationChangedListener {
    private MainActivity mainActivity;

    public NavigationControllerListener(MainActivity mainActivity){
        this.mainActivity = mainActivity;
    }

    @Override
    public void onDestinationChanged(@NonNull NavController controller,
                                     @NonNull NavDestination destination, @Nullable Bundle arguments) {
        this.mainActivity.currentActivityId = destination.getId();
        this.mainActivity.invalidateOptionsMenu();
    }
}
