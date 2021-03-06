package com.example.receiptas.ui.history;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.receiptas.MainActivity;
import com.example.receiptas.MainViewModel;
import com.example.receiptas.R;
import com.example.receiptas.model.domain_model.Receipt;

import java.util.List;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class HistoryFragment extends Fragment {

    private MainViewModel mainViewModel;
    private RecyclerView historyRecyclerView;
    private ReceiptAdapter adapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setHasOptionsMenu(true);
        mainViewModel = new ViewModelProvider(getActivity()).get(MainViewModel.class);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_history, container, false);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        ((MainActivity)getActivity()).getToolbar().setOverflowIcon(getContext().getDrawable(R.drawable.baseline_sort_24));
        inflater.inflate(R.menu.drawer, menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getTitle() == null){
            return super.onOptionsItemSelected(item);
        } else {
            return this.adapter.setSortMethod(item.getItemId());
        }
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        historyRecyclerView = view.findViewById(R.id.history_recycler_view);
        mainViewModel.getReceipts().observe(getViewLifecycleOwner(), receiptListUpdateObserver);
        this.configureRecyclerView();
    }

    private void configureRecyclerView() {
        adapter = new ReceiptAdapter(this.mainViewModel.getReceipts().getValue(), onReceiptClicked, R.id.sort_receipt_alphabetical);
        historyRecyclerView.setAdapter(adapter);
        historyRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
    }

    private final OnRecyclerViewItemClickListener<String> onReceiptClicked  = (itemId, item) -> {
        HistoryFragmentDirections.ShowReceiptDetail action = HistoryFragmentDirections.showReceiptDetail(
                itemId,
                this.mainViewModel.getReceipts().getValue().get(itemId).getName());
        Navigation.findNavController(getView()).navigate(action);
    };

    private final Observer<List<Receipt>> receiptListUpdateObserver = new Observer<List<Receipt>>() {
        @Override
        public void onChanged(List<Receipt> receipts) {
            adapter.notifyDataSetChanged();
        }
    };
}