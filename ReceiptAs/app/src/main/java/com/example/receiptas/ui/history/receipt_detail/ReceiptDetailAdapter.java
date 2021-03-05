package com.example.receiptas.ui.history.receipt_detail;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class ReceiptDetailAdapter extends FragmentStateAdapter {
    private final int pageCount;

    public ReceiptDetailAdapter(@NonNull Fragment fragment, int pageCount) {
        super(fragment);
        this.pageCount = pageCount;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        Fragment fragment;
        switch (position) {
            case 0:
                fragment = ReceiptDetailProductsFragment.newInstance("", "");
                break;
            default:
                fragment = ReceiptDetailSummaryFragment.newInstance("", "");
        }

        return fragment;
    }

    @Override
    public int getItemCount() {
        return pageCount;
    }
}
