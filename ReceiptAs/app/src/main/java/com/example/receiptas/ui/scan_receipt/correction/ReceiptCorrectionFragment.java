package com.example.receiptas.ui.scan_receipt.correction;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;
import androidx.viewpager2.widget.ViewPager2;

import com.example.receiptas.MainActivity;
import com.example.receiptas.R;
import com.example.receiptas.model.util.DataState;
import com.example.receiptas.ui.history.receipt_detail.ReceiptDetailProductsFragment;
import com.example.receiptas.ui.history.receipt_detail.ReceiptDetailSummaryFragment;
import com.example.receiptas.ui.scan_receipt.ScanReceiptViewModel;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

public class ReceiptCorrectionFragment extends Fragment {

    private TabLayout tabLayout;
    private ViewPager2 viewPager;
    private String[] tabsNames;
    private ProgressBar progressBar;
    private ItemCorrectionViewModel itemCorrectionViewModel;
    private ScanReceiptViewModel scanReceiptViewModel;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setHasOptionsMenu(true);
        this.scanReceiptViewModel = new ViewModelProvider(getActivity()).get(ScanReceiptViewModel.class);
        this.itemCorrectionViewModel = new ViewModelProvider(this).get(ItemCorrectionViewModel.class);
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

        this.progressBar = view.findViewById(R.id.progressBar);

        this.itemCorrectionViewModel.getCorrectableItems().observe(this.getViewLifecycleOwner(), correctableItemObserver);
        this.scanReceiptViewModel.parseTextFromImages();

        this.scanReceiptViewModel.getItems().getValue().getState().observe(this.getViewLifecycleOwner(), itemsObserver);
        this.scanReceiptViewModel.getPrices().getValue().getState().observe(this.getViewLifecycleOwner(), pricesObserver);

        itemsObserver.onChanged(this.scanReceiptViewModel.getItems().getValue().getState().getValue());
        pricesObserver.onChanged(this.scanReceiptViewModel.getPrices().getValue().getState().getValue());
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.validate, menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.validate_button) {
            NavDirections action = ReceiptCorrectionFragmentDirections.correctionToParticipant();
            Navigation.findNavController(getView()).navigate(action);
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

    private final Observer<DataState.State> itemsObserver = new Observer<DataState.State>() {
        @Override
        public void onChanged(DataState.State dataState) {
            switch(dataState) {
                case SUCCESS:
                    itemCorrectionViewModel.setCorrectableItemsFromList(scanReceiptViewModel.getItems().getValue().getData());
                    displayProgressBar(false);
                    break;
                case ERROR:
                    scanReceiptViewModel.getPrices().getValue().getState().removeObserver(pricesObserver);
                    openBlockingDialog(
                        R.string.error_parsing_items,
                        scanReceiptViewModel.getItems().getValue().getError().getMessage(),
                        R.string.dialog_positive,
                        null,
                        R.string.dialog_negative,
                        null,
                        true
                    );
                    displayProgressBar(false);
                    break;
                case LOADING:
                    displayProgressBar(true);
                    break;
                default:
                    //do nothing
            }
        }
    };

    private void displayProgressBar(boolean isDisplayed) {
        this.progressBar.setVisibility(isDisplayed ? View.VISIBLE : View.GONE);
    }

    private final Observer<DataState.State> pricesObserver = new Observer<DataState.State>() {
        @Override
        public void onChanged(DataState.State dataState) {
            switch(dataState) {
                case SUCCESS:
                    try {
                        itemCorrectionViewModel.setPricesFromParsedText(scanReceiptViewModel.getPrices().getValue().getData());
                    } catch (Exception e) {
                        scanReceiptViewModel.getPrices().getValue().setError(e);
                    }
                    break;
                case ERROR:
                    scanReceiptViewModel.getItems().getValue().getState().removeObserver(itemsObserver);
                    openBlockingDialog(
                        R.string.error_parsing_prices,
                        scanReceiptViewModel.getPrices().getValue().getError().getMessage(),
                        R.string.dialog_positive,
                        null,
                        R.string.dialog_negative,
                        null,
                        true
                    );
                    break;
                default:
                    //do nothing
            }
        }
    };

    private void openBlockingDialog(
        int titleId,
        String message,
        int positiveMessageId,
        DialogInterface.OnClickListener positiveListener,
        int negativeMessageId,
        DialogInterface.OnClickListener negativeListener,
        boolean navigateUp
    ) {
        AlertDialog.Builder blockingDialog = new AlertDialog.Builder(getContext());
        blockingDialog.setTitle(titleId);
        blockingDialog.setMessage(message);
        blockingDialog.setPositiveButton(positiveMessageId, positiveListener);
        blockingDialog.setNegativeButton(negativeMessageId, negativeListener);
        blockingDialog.create().show();

        if (navigateUp) {
            Navigation.findNavController(this.getView()).navigateUp();
        }
    }
}