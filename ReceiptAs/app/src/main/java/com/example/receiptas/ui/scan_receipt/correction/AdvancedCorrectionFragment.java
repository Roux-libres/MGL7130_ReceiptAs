package com.example.receiptas.ui.scan_receipt.correction;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.receiptas.R;

public class AdvancedCorrectionFragment extends Fragment {

    private final ItemCorrectionViewModel itemCorrectionViewModel;

    public AdvancedCorrectionFragment(ItemCorrectionViewModel itemCorrectionViewModel) {
        this.itemCorrectionViewModel = itemCorrectionViewModel;
    }

    public static AdvancedCorrectionFragment newInstance(ItemCorrectionViewModel itemCorrectionViewModel) {
        return new AdvancedCorrectionFragment(itemCorrectionViewModel);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_advanced_correction, container, false);
    }
}