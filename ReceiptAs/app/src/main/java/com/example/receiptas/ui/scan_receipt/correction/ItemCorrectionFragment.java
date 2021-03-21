package com.example.receiptas.ui.scan_receipt.correction;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.receiptas.MainActivity;
import com.example.receiptas.R;
import com.example.receiptas.model.util.DataState;
import com.example.receiptas.ui.history.HistoryFragmentDirections;
import com.example.receiptas.ui.history.OnRecyclerViewItemClickListener;
import com.example.receiptas.ui.history.ReceiptAdapter;
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

        this.itemCorrectionViewModel.getCorrectableItems().observe(getViewLifecycleOwner(), correctableItemObserver);
        if(this.scanReceiptViewModel.getItems().getValue().getState().getValue() == DataState.State.SUCCESS) {
            this.itemCorrectionViewModel.setCorrectableItemsFromList(this.scanReceiptViewModel.getItems().getValue().getData());
        } else {
            this.scanReceiptViewModel.getItems().getValue().getState().observe(getViewLifecycleOwner(), itemsObserver);
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.validate_button:
                //TODO navigation + consoliadation dans shared viewmodel (TheReceipt + DataState items ?)
                System.out.println(itemCorrectionViewModel.getCorrectedItems());
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void configureRecyclerView() {
        this.itemRecyclerView.setAdapter(new ItemAdapter(new ArrayList<>(), onItemOptionSelected, getContext()));
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
                case R.id.deleteItem:
                    item.setDeleted(true);
                    itemRecyclerView.getAdapter().notifyItemChanged(itemId);
                    return true;
                case R.id.undeleteItem:
                    item.setDeleted(false);
                    itemRecyclerView.getAdapter().notifyItemChanged(itemId);
                    return true;
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
        menu.getMenu().findItem(R.id.deleteItem).setVisible(!item.isDeleted());
        menu.getMenu().findItem(R.id.undeleteItem).setVisible(item.isDeleted());

        boolean isCombined = item.getCombinedItem() == null ? false : true;
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

    private final Observer<ArrayList<ItemCorrectionViewModel.CorrectableItem>> correctableItemObserver =
        new Observer<ArrayList<ItemCorrectionViewModel.CorrectableItem>>() {
            @Override
            public void onChanged(ArrayList<ItemCorrectionViewModel.CorrectableItem> correctableItems) {
                itemRecyclerView.setAdapter(new ItemAdapter(correctableItems, onItemOptionSelected, getContext()));
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
                    displayProgressBar(false);
                    scanReceiptViewModel.getItems().getValue().getError().printStackTrace();
                    break;
                case LOADING:
                    displayProgressBar(true);
                    break;
                default:
                    //do nothing
            }
        }
    };
}