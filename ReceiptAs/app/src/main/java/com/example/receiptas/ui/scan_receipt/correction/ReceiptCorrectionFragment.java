package com.example.receiptas.ui.scan_receipt.correction;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import com.example.receiptas.MainActivity;
import com.example.receiptas.R;
import com.example.receiptas.ui.history.receipt_detail.ReceiptDetailProductsFragment;
import com.example.receiptas.ui.history.receipt_detail.ReceiptDetailSummaryFragment;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

public class ReceiptCorrectionFragment extends Fragment {

    private TabLayout tabLayout;
    private ViewPager2 viewPager;
    private String[] tabsNames;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setHasOptionsMenu(true);
        this.tabsNames = getResources().getStringArray(R.array.receipt_correction_tabs_names);

        //TODO adapter à la tablette
        if(((MainActivity) getActivity()).isTablet()) {
            Bundle arguments = new Bundle();
            //arguments.putInt();

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

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //TODO adapter à la tablette
        if(((MainActivity) getActivity()).isTablet()) {
            TextView itemCorrectionHeader = view.findViewById(R.id.item_correction_header);
            itemCorrectionHeader.setText(this.tabsNames[0]);

            TextView advancedCorrectionHeader = view.findViewById(R.id.advanced_correction_header);
            advancedCorrectionHeader.setText(this.tabsNames[1]);
        } else {
            tabLayout = view.findViewById(R.id.tab_layout);
            viewPager = view.findViewById(R.id.pager);

            tabLayout.addOnTabSelectedListener(tabSelectedListener);
            viewPager.setAdapter(new ReceiptCorrectionAdapter(this, tabLayout.getTabCount()));

            new TabLayoutMediator(tabLayout, viewPager, (tab, position) -> tab.setText(tabsNames[position])).attach();
        }
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.validate, menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.validate_button) {
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }

    private final TabLayout.OnTabSelectedListener tabSelectedListener = new TabLayout.OnTabSelectedListener() {
        @Override
        public void onTabSelected(TabLayout.Tab tab) {

        }

        @Override
        public void onTabUnselected(TabLayout.Tab tab) {

        }

        @Override
        public void onTabReselected(TabLayout.Tab tab) {

        }
    };
}