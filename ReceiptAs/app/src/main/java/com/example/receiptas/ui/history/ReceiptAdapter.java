package com.example.receiptas.ui.history;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.receiptas.R;
import com.example.receiptas.model.domain_model.Receipt;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;

public class ReceiptAdapter extends RecyclerView.Adapter<ReceiptViewHolder> {

    private final List<Receipt> localDataSet;
    private final OnRecyclerViewItemClickListener listener;

    public ReceiptAdapter (List<Receipt> dataSet, OnRecyclerViewItemClickListener listener) {
        this.localDataSet = dataSet;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ReceiptViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.history_row_item, parent, false);
        return new ReceiptViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReceiptViewHolder holder, int position) {
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        holder.getLeftTextView().setText(localDataSet.get(position).getName());
        holder.getRightTextView().setText(dateFormat.format(localDataSet.get(position).getDate()));
        holder.bindListener(position, localDataSet.get(position).getName(), this.listener);
    }

    @Override
    public int getItemCount() {
        return localDataSet.size();
    }
}
