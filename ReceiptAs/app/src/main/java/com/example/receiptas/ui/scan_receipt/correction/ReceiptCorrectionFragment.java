package com.example.receiptas.ui.scan_receipt.correction;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import com.example.receiptas.MainActivity;
import com.example.receiptas.MainViewModel;
import com.example.receiptas.R;
import com.example.receiptas.ui.history.receipt_detail.ReceiptDetailProductsFragment;
import com.example.receiptas.ui.history.receipt_detail.ReceiptDetailSummaryFragment;
import com.google.android.material.tabs.TabLayout;

public class ReceiptCorrectionFragment extends Fragment {

    private TabLayout tabLayout;
    private ViewPager2 viewPager;
    private String[] tabsNames;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setHasOptionsMenu(true);
        this.tabsNames = getResources().getStringArray(/*R.array.receipt_detail_tabs_names*/);

        if(((MainActivity) getActivity()).isTablet()) {
            Bundle arguments = new Bundle();
            arguments.putInt();

            getActivity().getSupportFragmentManager().beginTransaction()
                .setReorderingAllowed(true)
                .add(R.id.include_fragment_receipt_detail_products,
                    ReceiptDetailProductsFragment.class,
                    arguments)
                .add(R.id.include_fragment_receipt_detail_summary,
                    ReceiptDetailSummaryFragment.class,
                    arguments)
                .commit();
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_receipt_correction, container, false);
    }
}