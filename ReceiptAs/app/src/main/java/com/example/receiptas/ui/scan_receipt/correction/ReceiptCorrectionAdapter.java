package com.example.receiptas.ui.scan_receipt.correction;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class ReceiptCorrectionAdapter extends FragmentStateAdapter {

    private final int pageCount;
    private final ReceiptCorrectionViewModel receiptCorrectionViewModel;

    public ReceiptCorrectionAdapter(@NonNull Fragment fragment, ReceiptCorrectionViewModel receiptCorrectionViewModel, int pageCount) {
        super(fragment);
        this.receiptCorrectionViewModel = receiptCorrectionViewModel;
        this.pageCount = pageCount;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        Fragment fragment;
        switch (position) {
            case 0:
                fragment = ItemCorrectionFragment.newInstance(this.receiptCorrectionViewModel);
                break;
            default:
                fragment = AdvancedCorrectionFragment.newInstance(this.receiptCorrectionViewModel);
        }

        return fragment;
    }

    @Override
    public int getItemCount() {
        return this.pageCount;
    }
}
