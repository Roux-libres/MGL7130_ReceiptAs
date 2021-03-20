package com.example.receiptas.ui.history;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.TextView;
import android.widget.Toolbar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.receiptas.MainActivity;
import com.example.receiptas.R;

import java.util.ArrayList;
import java.util.List;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class HistoryFragment extends Fragment {

    private HistoryViewModel historyViewModel;
    private RecyclerView historyRecyclerView;
    private ReceiptAdapter adapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        historyViewModel = new ViewModelProvider(this).get(HistoryViewModel.class);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_history, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        historyRecyclerView = view.findViewById(R.id.history_recycler_view);
        historyViewModel.getReceipts().observe(getViewLifecycleOwner(), receiptListUpdateObserver);
        this.configureRecyclerView();

        List<String> list = new ArrayList<>();
        list.add("test receipt");
        historyViewModel.getReceipts().setValue(list);
    }

    private void configureRecyclerView() {
        adapter = new ReceiptAdapter(new ArrayList<>(), onReceiptClicked);
        historyRecyclerView.setAdapter(adapter);
        historyRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
    }

    private final OnRecyclerViewItemClickListener<String> onReceiptClicked  = (itemId, item) -> {
        HistoryFragmentDirections.ShowReceiptDetail action = HistoryFragmentDirections.showReceiptDetail();
        action.setReceipt(item);
        action.setReceiptName(item);
        Navigation.findNavController(getView()).navigate(action);
    };

    private final Observer<List<String>> receiptListUpdateObserver = new Observer<List<String>>() {
        @Override
        public void onChanged(List<String> receipts) {
            historyRecyclerView.setAdapter(new ReceiptAdapter(receipts, onReceiptClicked));
        }
    };
}