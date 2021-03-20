package com.example.receiptas.ui.scan_receipt.correction;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

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
    private RecyclerView itemRecyclerView;
    private ItemAdapter adapter;
    private ProgressBar progressBar;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        scanReceiptViewModel = new ViewModelProvider(getActivity()).get(ScanReceiptViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_item_correction, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.progressBar = view.findViewById(R.id.progressBar);

        TextView informationMessage = view.findViewById(R.id.information_message);
        informationMessage.setText(getResources().getString(R.string.information_message_item_correction));

        this.itemRecyclerView = view.findViewById(R.id.itemRecyclerView);
        this.configureRecyclerView();
    }

    private void configureRecyclerView() {
        if(this.scanReceiptViewModel.getItems().getValue().getState().getValue() == DataState.State.SUCCESS) {
            System.out.println("ALREADY LOADED");
            this.adapter = new ItemAdapter(this.scanReceiptViewModel.getItems().getValue().getData(), onItemOptionSelected);
        } else {
            this.adapter = new ItemAdapter(new ArrayList<>(), onItemOptionSelected);
            this.scanReceiptViewModel.getItems().getValue().getState().observe(getViewLifecycleOwner(), itemsObserver);
        }
        this.itemRecyclerView.setAdapter(adapter);
        this.itemRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
    }

    private final OnRecyclerViewItemClickListener onItemOptionSelected  = item -> {
        //TODO PROCESS ITEM MENU ACTION
    };

    private final Observer<DataState.State> itemsObserver = new Observer<DataState.State>() {
        @Override
        public void onChanged(DataState.State dataState) {
            switch(dataState) {
                case SUCCESS:
                    displayProgressBar(false);
                    itemRecyclerView.setAdapter(
                        new ItemAdapter(scanReceiptViewModel.getItems().getValue().getData(), onItemOptionSelected)
                    );
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

    private void displayProgressBar(boolean isDisplayed) {
        this.progressBar.setVisibility(isDisplayed ? View.VISIBLE : View.GONE);
    }
}