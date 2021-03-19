package com.example.receiptas.ui.scan_receipt.correction;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.receiptas.R;
import com.example.receiptas.ui.history.HistoryFragmentDirections;
import com.example.receiptas.ui.history.OnRecyclerViewItemClickListener;
import com.example.receiptas.ui.history.ReceiptAdapter;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ItemCorrectionFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ItemCorrectionFragment extends Fragment {
    private RecyclerView itemRecyclerView;
    private ItemAdapter adapter;

    public ItemCorrectionFragment() {
        // Required empty public constructor
    }

    public static ItemCorrectionFragment newInstance() {
        ItemCorrectionFragment fragment = new ItemCorrectionFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
        TextView informationMessage = view.findViewById(R.id.information_message);
        informationMessage.setText(getResources().getString(R.string.information_message_item_correction));

        this.itemRecyclerView = view.findViewById(R.id.itemRecyclerView);
        this.configureRecyclerView();
    }

    private void configureRecyclerView() {
        this.adapter = new ItemAdapter(new ArrayList<>(), onItemOptionSelected);
        this.itemRecyclerView.setAdapter(adapter);
        this.itemRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
    }

    private final OnRecyclerViewItemClickListener onItemOptionSelected  = item -> {
        //TODO PROCESS ITEM MENU ACTION
    };
}