package com.example.receiptas.ui.scan_receipt.correction;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class ReceiptCorrectionAdapter extends FragmentStateAdapter {

    private final int pageCount;

    public ReceiptCorrectionAdapter(@NonNull Fragment fragment, int pageCount) {
        super(fragment);
        this.pageCount = pageCount;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        Fragment fragment;
        switch (position) {
            case 0:
                fragment = ItemCorrectionFragment.newInstance();
                break;
            default:
                fragment = AdvancedCorrectionFragment.newInstance();
        }

        return fragment;
    }

    @Override
    public int getItemCount() {
        return this.pageCount;
    }
}
