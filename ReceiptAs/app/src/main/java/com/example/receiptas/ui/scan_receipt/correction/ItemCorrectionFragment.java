package com.example.receiptas.ui.scan_receipt.correction;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.receiptas.R;
import com.example.receiptas.model.domain_model.Item;
import com.example.receiptas.model.util.DataState;
import com.example.receiptas.ui.history.OnRecyclerViewItemClickListener;
import com.example.receiptas.ui.scan_receipt.ScanReceiptViewModel;

import java.util.ArrayList;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class ItemCorrectionFragment extends Fragment {

    private ScanReceiptViewModel scanReceiptViewModel;
    private ItemCorrectionViewModel itemCorrectionViewModel;
    private RecyclerView itemRecyclerView;
    private ProgressBar progressBar;
    private Toolbar toolbar;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setHasOptionsMenu(true);
        scanReceiptViewModel = new ViewModelProvider(getActivity()).get(ScanReceiptViewModel.class);
        itemCorrectionViewModel = new ViewModelProvider(this).get(ItemCorrectionViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_item_correction, container, false);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.validate, menu);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.progressBar = view.findViewById(R.id.progressBar);

        TextView informationMessage = view.findViewById(R.id.information_message);
        informationMessage.setText(getResources().getString(R.string.information_message_item_correction));

        this.itemRecyclerView = view.findViewById(R.id.itemRecyclerView);
        this.configureRecyclerView();

        this.itemCorrectionViewModel.getCorrectableItems().observe(this.getViewLifecycleOwner(), correctableItemObserver);
        this.scanReceiptViewModel.parseTextFromImages();
        this.scanReceiptViewModel.getItems().getValue().getState().observe(this.getViewLifecycleOwner(), itemsObserver);
        this.scanReceiptViewModel.getPrices().getValue().getState().observe(this.getViewLifecycleOwner(), pricesObserver);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.validate_button
            && scanReceiptViewModel.getPrices().getValue().getState().getValue() == DataState.State.SUCCESS
            && scanReceiptViewModel.getItems().getValue().getState().getValue() == DataState.State.SUCCESS
        ) {
            ArrayList<String> correctedItems = itemCorrectionViewModel.getCorrectedItems();
            openBlockingDialog(
                R.string.item_correction_dialog_validate_title,
                itemCorrectionViewModel.getPreview(
                    correctedItems,
                    itemCorrectionViewModel.getPrices(),
                    scanReceiptViewModel.getReceipt().getCurrency().getSymbol().charAt(0),
                    this.getContext()
                ),
                R.string.item_correction_dialog_validate,
                (dialog, which) -> {
                    int referenceSize = Math.max(correctedItems.size(), itemCorrectionViewModel.getPrices().size());
                    ArrayList<Item> items = new ArrayList<>();
                    for(int i = 0; i < referenceSize; i++) {
                        items.add(new Item(
                            i < correctedItems.size() ? correctedItems.get(i) : "no item",
                            i < itemCorrectionViewModel.getPrices().size() ? itemCorrectionViewModel.getPrices().get(i) : 0,
                            new ArrayList<>()
                        ));
                    }
                    this.scanReceiptViewModel.getReceipt().setItems(items);

                    NavDirections action = ItemCorrectionFragmentDirections.actionToAddingParticipantsFragment();
                    Navigation.findNavController(getView()).navigate(action);
                },
                R.string.dialog_negative,
                null,
                false);
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }

    private void configureRecyclerView() {
        this.itemRecyclerView.setAdapter(new ItemAdapter(new ArrayList<>(), onItemClick, onItemOptionSelected, getContext()));
        this.itemRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
    }

    private void displayProgressBar(boolean isDisplayed) {
        this.progressBar.setVisibility(isDisplayed ? View.VISIBLE : View.GONE);
    }

    private void showPopup(View v, int itemId) {
        PopupMenu popup = new PopupMenu(getContext(), v);
        ItemCorrectionViewModel.CorrectableItem item = itemCorrectionViewModel.getCorrectableItems().getValue().get(itemId);
        popup.setOnMenuItemClickListener(menuItem -> {
            switch(menuItem.getItemId()) {
                case R.id.combineAbove:
                    combineItemIntoRootItem(itemId-1, itemId);
                    return true;
                case R.id.combineBelow:
                    combineItemIntoRootItem(itemId, itemId+1);
                    return true;
                case R.id.detachFirst:
                    detachItemFromRootItem(itemId, false);
                    return true;
                case R.id.detachLast:
                    detachItemFromRootItem(itemId, true);
                    return true;
                default:
                    return false;
            }
        });

        this.configurePopupMenuItems(popup, item);
        popup.show();
    }

    private void configurePopupMenuItems(PopupMenu menu, ItemCorrectionViewModel.CorrectableItem item) {
        menu.inflate(R.menu.item_correction_popup);

        boolean isCombined = item.getCombinedItem() != null;
        menu.getMenu().findItem(R.id.detachFirst).setVisible(isCombined);
        menu.getMenu().findItem(R.id.detachLast).setVisible(isCombined);
    }

    private void combineItemIntoRootItem(int rootItemId, int itemId) {
        ArrayList<ItemCorrectionViewModel.CorrectableItem> items = itemCorrectionViewModel.getCorrectableItems().getValue();
        if(rootItemId >= 0 && itemId < items.size()) {
            items.get(rootItemId).combineItem(items.get(itemId));
            items.remove(itemId);
            itemRecyclerView.getAdapter().notifyItemRemoved(itemId);
            itemRecyclerView.getAdapter().notifyItemRangeChanged(rootItemId, items.size());
        }
    }

    private void detachItemFromRootItem(int rootItemId, boolean detachLast) {
        ArrayList<ItemCorrectionViewModel.CorrectableItem> items = itemCorrectionViewModel.getCorrectableItems().getValue();

        if(detachLast) {
            items.add(rootItemId+1, items.get(rootItemId).popLastCombinedItem());
        } else {
            items.add(rootItemId+1, items.get(rootItemId).popCombinedItem());

        }
        itemRecyclerView.getAdapter().notifyItemInserted(rootItemId);
        itemRecyclerView.getAdapter().notifyItemRangeChanged(rootItemId, items.size());
    }

    private final OnRecyclerViewItemClickListener<View> onItemOptionSelected  = (itemId, view) -> {
        showPopup(view, itemId);
    };

    private final OnRecyclerViewItemClickListener<ItemCorrectionViewModel.CorrectableItem> onItemClick  = (itemId, item) -> {
            item.setDeleted(!item.isDeleted());
            itemRecyclerView.getAdapter().notifyItemChanged(itemId);
    };

    private final Observer<ArrayList<ItemCorrectionViewModel.CorrectableItem>> correctableItemObserver =
        new Observer<ArrayList<ItemCorrectionViewModel.CorrectableItem>>() {
            @Override
            public void onChanged(ArrayList<ItemCorrectionViewModel.CorrectableItem> correctableItems) {
                itemRecyclerView.setAdapter(new ItemAdapter(correctableItems, onItemClick, onItemOptionSelected, getContext()));
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
                        itemCorrectionViewModel.setPricesFromParsedText(scanReceiptViewModel.getPrices().getValue().getData());
                    } catch (Exception e) {
                        scanReceiptViewModel.getPrices().getValue().setError(e);
                    }
                    break;
                case ERROR:
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
            //TODO RESOLVE CRASH WHEN TIME OUT GO BACK TO PROCESS IMAGE FRAGMENT
            Navigation.findNavController(this.getView()).navigateUp();
        }
    }
}