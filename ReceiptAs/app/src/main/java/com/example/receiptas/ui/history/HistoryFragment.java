package com.example.receiptas.ui.history;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavDirections;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.receiptas.R;

import java.util.ArrayList;
import java.util.List;

public class HistoryFragment extends Fragment {

    private HistoryViewModel historyViewModel;
    private RecyclerView historyRecyclerView;
    private ReceiptAdapter adapter;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        historyViewModel =
                new ViewModelProvider(this).get(HistoryViewModel.class);
        View root = inflater.inflate(R.layout.fragment_history, container, false);
        historyViewModel.getReceipts().observe(getViewLifecycleOwner(), receiptListUpdateObserver);
        historyRecyclerView = root.findViewById(R.id.history_recycler_view);
        this.configureRecyclerView();
        return root;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //TODO maybe
    }

    private void configureRecyclerView() {
        adapter = new ReceiptAdapter(new ArrayList<String>(), onReceiptClicked);
        historyRecyclerView.setAdapter(adapter);
        historyRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
    }

    private final  OnRecyclerViewItemClickListener onReceiptClicked  = new OnRecyclerViewItemClickListener() {
        @Override
        public void onItemClick(String item) {
            //TODO navigate to receipt details
            //NavDirections action = HistoryFragmentDirections
        }
    };

    private final Observer<List<String>> receiptListUpdateObserver = new Observer<List<String>>() {
        @Override
        public void onChanged(List<String> receipts) {
            historyRecyclerView.setAdapter(new ReceiptAdapter(receipts, onReceiptClicked));
        }
    };
}