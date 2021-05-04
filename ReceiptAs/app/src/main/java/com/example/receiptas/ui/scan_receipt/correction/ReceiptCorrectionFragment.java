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
import com.example.receiptas.model.domain_model.Item;
import com.example.receiptas.model.util.DataState;
import com.example.receiptas.ui.scan_receipt.ScanReceiptViewModel;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.ArrayList;

public class ReceiptCorrectionFragment extends Fragment {

    private TabLayout tabLayout;
    private ViewPager2 viewPager;
    private String[] tabsNames;
    private ProgressBar progressBar;
    private ReceiptCorrectionViewModel receiptCorrectionViewModel;
    private ScanReceiptViewModel scanReceiptViewModel;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setHasOptionsMenu(true);
        this.scanReceiptViewModel = new ViewModelProvider(getActivity()).get(ScanReceiptViewModel.class);
        this.receiptCorrectionViewModel = new ViewModelProvider(this).get(ReceiptCorrectionViewModel.class);
        this.tabsNames = getResources().getStringArray(R.array.receipt_correction_tabs_names);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_receipt_correction, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if(((MainActivity) getActivity()).isTablet()) {
            getActivity().getSupportFragmentManager().beginTransaction()
                .setReorderingAllowed(true)
                .add(R.id.include_fragment_item_correction,
                    ItemCorrectionFragment.newInstance(this.receiptCorrectionViewModel))
                .add(R.id.include_fragment_advanced_correction,
                    AdvancedCorrectionFragment.newInstance(this.receiptCorrectionViewModel))
                .commit();

            TextView itemCorrectionHeader = view.findViewById(R.id.item_correction_header);
            itemCorrectionHeader.setText(this.tabsNames[0]);

            TextView advancedCorrectionHeader = view.findViewById(R.id.advanced_correction_header);
            advancedCorrectionHeader.setText(this.tabsNames[1]);
        } else {
            tabLayout = view.findViewById(R.id.tab_layout);
            viewPager = view.findViewById(R.id.pager);

            tabLayout.addOnTabSelectedListener(tabSelectedListener);
            viewPager.setAdapter(new ReceiptCorrectionAdapter(this, this.receiptCorrectionViewModel, tabLayout.getTabCount()));

            new TabLayoutMediator(tabLayout, viewPager, (tab, position) -> tab.setText(tabsNames[position])).attach();
        }

        this.progressBar = view.findViewById(R.id.progressBar);

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
        if (item.getItemId() == R.id.validate_button
            && scanReceiptViewModel.getPrices().getValue().getState().getValue() == DataState.State.SUCCESS
            && scanReceiptViewModel.getItems().getValue().getState().getValue() == DataState.State.SUCCESS
        ) {
            boolean receiptSaved = this.saveCorrections();

            float calculatedTotal = scanReceiptViewModel.getReceipt().getTotalAmount();
            if (!receiptSaved){
                openBlockingDialog(
                    R.string.receipt_correction_save_error,
                    getContext().getResources().getString(R.string.receipt_correction_save_error_help),
                    R.string.item_correction_dialog_validate,
                    null,
                    R.string.dialog_negative,
                    null,
                    false
                );
            } else if(calculatedTotal == scanReceiptViewModel.getReceiptSpecifiedPrice().getValue()) {
                NavDirections action = ReceiptCorrectionFragmentDirections.correctionToParticipant();
                Navigation.findNavController(getView()).navigate(action);
            } else {
                openBlockingDialog(
                    R.string.item_correction_dialog_validate_title,
                    getContext().getResources().getString(R.string.receipt_correction_dialog_total_caution)
                        .concat("\n\nCalculated total = " + calculatedTotal)
                        .concat("\n\nSpecified total = " + scanReceiptViewModel.getReceiptSpecifiedPrice().getValue()),
                    R.string.item_correction_dialog_validate,
                    (dialog, which) -> {
                        NavDirections action = ReceiptCorrectionFragmentDirections.correctionToParticipant();
                        Navigation.findNavController(getView()).navigate(action);
                    },
                    R.string.dialog_negative,
                    null,
                    false
                );
            }
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }

    private boolean saveCorrections() {
        boolean allRight = false;

        try {
            ArrayList<Item> items = new ArrayList<>();
            for(int i = 0; i < this.receiptCorrectionViewModel.getCorrectedItems().getValue().size(); i++) {
                items.add(
                    new Item(
                        this.receiptCorrectionViewModel.getCorrectedItems().getValue().get(i),
                        Float.parseFloat(this.receiptCorrectionViewModel.getPrices().getValue().get(i)),
                        new ArrayList<>()
                    )
                );
            }
            this.scanReceiptViewModel.getReceipt().setItems(items);
            allRight = true;
        } catch(Exception e) {
            System.out.println(e);
            this.scanReceiptViewModel.getReceipt().setItems(new ArrayList<>());
        }

        return allRight;
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

                    if(scanReceiptViewModel.getReceipt().getItems().isEmpty()) {
                        receiptCorrectionViewModel.setCorrectableItemsFromList(scanReceiptViewModel.getItems().getValue().getData(), getContext());
                    } else {
                        ArrayList<String> items = new ArrayList<>();
                        for(Item item : scanReceiptViewModel.getReceipt().getItems()) {
                            items.add(item.getName());
                        }
                        receiptCorrectionViewModel.setCorrectableItemsFromList(items, getContext());
                    }


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

    private final Observer<DataState.State> pricesObserver = new Observer<DataState.State>() {
        @Override
        public void onChanged(DataState.State dataState) {
            switch(dataState) {
                case SUCCESS:
                    try {
                        if(scanReceiptViewModel.getReceipt().getItems().isEmpty()) {
                            receiptCorrectionViewModel.setPricesFromParsedText(scanReceiptViewModel.getPrices().getValue().getData(), getContext());
                        } else {
                            ArrayList<String> prices = new ArrayList<>();
                            for(Item item : scanReceiptViewModel.getReceipt().getItems()) {
                                prices.add(Float.toString(item.getPrice()));
                            }
                            receiptCorrectionViewModel.setPricesFromParsedText(prices, getContext());
                        }
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

    private void displayProgressBar(boolean isDisplayed) {
        this.progressBar.setVisibility(isDisplayed ? View.VISIBLE : View.GONE);
    }

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