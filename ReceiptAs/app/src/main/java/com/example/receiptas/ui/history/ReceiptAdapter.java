package com.example.receiptas.ui.history;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.receiptas.R;

import java.util.List;

public class ReceiptAdapter extends RecyclerView.Adapter<ReceiptViewHolder> {

    private List<String> localDataSet;

    public ReceiptAdapter (List<String> dataSet) {
        localDataSet = dataSet;
    }

    @NonNull
    @Override
    public ReceiptViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.history_row_item, parent, false);
        return new ReceiptViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReceiptViewHolder holder, int position) {
        holder.getLeftTextView().setText(localDataSet.get(position));
        holder.getRightTextView().setText(localDataSet.get(position));
    }

    @Override
    public int getItemCount() {
        return localDataSet.size();
    }
}
