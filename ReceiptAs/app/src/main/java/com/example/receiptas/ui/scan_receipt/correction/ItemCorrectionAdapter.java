package com.example.receiptas.ui.scan_receipt.correction;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.receiptas.ui.history.receipt_detail.ReceiptDetailProductsFragment;
import com.example.receiptas.ui.history.receipt_detail.ReceiptDetailSummaryFragment;

public class ItemCorrectionAdapter extends FragmentStateAdapter {

    private final int pageCount;

    public ItemCorrectionAdapter(@NonNull Fragment fragment, int pageCount) {
        super(fragment);
        this.pageCount = pageCount;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        Fragment fragment;
        switch (position) {
            case 0:
                fragment = ReceiptDetailProductsFragment.newInstance();
                break;
            default:
                fragment = ReceiptDetailSummaryFragment.newInstance();
        }

        return fragment;
    }

    @Override
    public int getItemCount() {
        return this.pageCount;
    }
}
