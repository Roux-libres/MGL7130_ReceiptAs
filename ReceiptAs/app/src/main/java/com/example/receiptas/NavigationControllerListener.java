package com.example.receiptas;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;

public class NavigationControllerListener implements NavController.OnDestinationChangedListener {
    private DrawerActivity drawerActivity;

    public NavigationControllerListener(DrawerActivity drawerActivity){
        this.drawerActivity = drawerActivity;
    }

    @Override
    public void onDestinationChanged(@NonNull NavController controller,
                                     @NonNull NavDestination destination, @Nullable Bundle arguments) {
        this.drawerActivity.currentActivityId = destination.getId();
        this.drawerActivity.invalidateOptionsMenu();
    }
}
