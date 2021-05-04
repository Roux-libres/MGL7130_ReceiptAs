package com.example.receiptas.ui.scan_receipt.correction;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.receiptas.R;
import com.example.receiptas.ui.history.OnRecyclerViewItemClickListener;

import java.util.ArrayList;

public class CorrectionAdapter extends RecyclerView.Adapter<CorrectionViewHolder>{
    private final ArrayList<String> correctedItems;
    private final ArrayList<String> prices;

    public CorrectionAdapter(
        ArrayList<String> dataSet,
        ArrayList<String> prices,
        OnRecyclerViewItemClickListener itemClickListener,
        Context context
    ) {
        this.correctedItems = dataSet;
        this.prices = prices;
    }

    @NonNull
    @Override
    public CorrectionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.history_row_item, parent, false);
        return new CorrectionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CorrectionViewHolder holder, int position) {
        System.out.println(this.correctedItems.size() + " : " + this.prices.size());
        holder.getLeftTextView().setText(correctedItems.get(position));
        holder.getRightTextView().setText(prices.get(position));
    }

    @Override
    public int getItemCount() {
        return Math.max(this.correctedItems.size(), this.prices.size());
    }
}
