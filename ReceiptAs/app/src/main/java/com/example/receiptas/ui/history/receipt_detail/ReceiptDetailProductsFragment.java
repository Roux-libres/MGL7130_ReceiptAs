package com.example.receiptas.ui.history.receipt_detail;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.receiptas.MainViewModel;
import com.example.receiptas.R;
import com.example.receiptas.ui.division.ItemDivisionAdapter;

public class ReceiptDetailProductsFragment extends Fragment {

    private MainViewModel mainViewModel;
    private int receiptId;

    public ReceiptDetailProductsFragment() {

    }

    public ReceiptDetailProductsFragment(int receiptId) {
        this.receiptId = receiptId;
    }

    public static ReceiptDetailProductsFragment newInstance(int receiptId) {
        ReceiptDetailProductsFragment fragment = new ReceiptDetailProductsFragment(receiptId);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.mainViewModel = new ViewModelProvider(getActivity()).get(MainViewModel.class);

        if(savedInstanceState != null){
            this.receiptId = savedInstanceState.getInt("receipt_id");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_receipt_detail_products, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        RecyclerView itemRecyclerView = view.findViewById(R.id.item_recycler_view);
        ItemDivisionAdapter itemDivisionAdapter = new ItemDivisionAdapter(
                this.mainViewModel.getReceipts().getValue().get(this.receiptId),
                getContext(),
                null);
        itemRecyclerView.setAdapter(itemDivisionAdapter);
        itemRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
    }
}