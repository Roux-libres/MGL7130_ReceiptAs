package com.example.receiptas.ui.history.receipt_detail;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.receiptas.MainActivity;
import com.example.receiptas.MainViewModel;
import com.example.receiptas.R;
import com.example.receiptas.model.domain_model.Receipt;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

public class ReceiptDetailFragment extends Fragment {

    private MainViewModel mainViewModel;
    private TabLayout tabLayout;
    private ViewPager2 viewPager;
    private String[] tabsNames;

    public static ReceiptDetailFragment newInstance() {
        return new ReceiptDetailFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.tabsNames = getResources().getStringArray(R.array.receipt_detail_tabs_names);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_receipt_detail, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if(((MainActivity) getActivity()).isTablet()) {
            TextView productsListHeader = getView().findViewById(R.id.products_list_header);
            productsListHeader.setText(this.tabsNames[0]);

            TextView summaryHeader = getView().findViewById(R.id.summary_header);
            summaryHeader.setText(this.tabsNames[1]);
        } else {
            tabLayout = view.findViewById(R.id.tab_layout);
            viewPager = view.findViewById(R.id.pager);

            tabLayout.addOnTabSelectedListener(tabSelectedListener);
            viewPager.setAdapter(new ReceiptDetailAdapter(this, tabLayout.getTabCount()));

            new TabLayoutMediator(tabLayout, viewPager, (tab, position) -> tab.setText(tabsNames[position])).attach();
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        this.mainViewModel = new ViewModelProvider(getActivity()).get(MainViewModel.class);
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