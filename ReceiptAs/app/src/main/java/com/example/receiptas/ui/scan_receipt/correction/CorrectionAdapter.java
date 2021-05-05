package com.example.receiptas.ui.scan_receipt.correction;

import android.content.Context;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.receiptas.R;
import com.example.receiptas.ui.history.OnRecyclerViewItemClickListener;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class CorrectionAdapter extends RecyclerView.Adapter<CorrectionViewHolder>{
    private final ArrayList<String> correctedItems;
    private final ArrayList<String> prices;
    private final OnRecyclerViewItemClickListener listener;
    private final Context context;
    private final DecimalFormat formatter;

    public CorrectionAdapter(
        ArrayList<String> dataSet,
        ArrayList<String> prices,
        OnRecyclerViewItemClickListener itemClickListener,
        Context context,
        DecimalFormat formatter
    ) {
        this.correctedItems = dataSet;
        this.prices = prices;
        this.listener = itemClickListener;
        this.context = context;
        this.formatter = formatter;
    }

    @NonNull
    @Override
    public CorrectionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.history_row_item, parent, false);
        return new CorrectionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CorrectionViewHolder holder, int position) {
        TypedValue textColor = new TypedValue();
        holder.getLeftTextView().setText(this.correctedItems.get(position));

        try {
            this.formatter.parse(this.formatter.format(this.formatter.parse(this.prices.get(position)).floatValue())).floatValue();
            context.getTheme().resolveAttribute(R.attr.correctionItemColor, textColor, true);

        }catch (Exception e) {
            System.out.println(e);
            context.getTheme().resolveAttribute(R.attr.correctionItemDeletedColor, textColor, true);
        }

        holder.getRightTextView().setTextColor(context.getResources().getColor(textColor.resourceId));
        holder.getRightTextView().setText(this.prices.get(position));
        holder.bindListener(position, this.correctedItems.get(position), this.listener);
    }

    @Override
    public int getItemCount() {
        return Math.max(this.correctedItems.size(), this.prices.size());
    }
}
