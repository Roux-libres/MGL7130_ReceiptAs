package com.example.receiptas.ui.scan_receipt.correction;

import android.content.Context;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.receiptas.R;
import com.example.receiptas.ui.history.OnRecyclerViewItemClickListener;

import java.util.List;

public class ItemAdapter extends RecyclerView.Adapter<ItemViewHolder>{

    private final List<ItemCorrectionViewModel.CorrectableItem> localDataSet;
    private final OnRecyclerViewItemClickListener<View> itemOptionListener;
    private final OnRecyclerViewItemClickListener<ItemCorrectionViewModel.CorrectableItem> itemClickListener;
    private final Context context;

    public ItemAdapter(
        List<ItemCorrectionViewModel.CorrectableItem> dataSet,
        OnRecyclerViewItemClickListener itemClickListener,
        OnRecyclerViewItemClickListener itemOptionListener,
        Context context
    ) {
        this.localDataSet = dataSet;
        this.itemOptionListener = itemOptionListener;
        this.itemClickListener = itemClickListener;
        this.context = context;
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.correction_row_item, parent, false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        if(this.localDataSet.get(position).isDeleted()) {
                holder.getItemLabel().setTextColor(context.getResources().getColor(R.color.red));
                holder.getItemLabel().setPaintFlags(holder.getItemLabel().getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        } else if(this.localDataSet.get(position).getCombinedItem() != null){
            holder.getItemLabel().setTextColor(context.getResources().getColor(R.color.purple_500));
        } else {
            holder.getItemLabel().setTextColor(context.getResources().getColor(R.color.black));
            holder.getItemLabel().setPaintFlags(holder.getItemLabel().getPaintFlags() & ~Paint.STRIKE_THRU_TEXT_FLAG);
        }

        holder.getItemLabel().setText(localDataSet.get(position).getLabel());
        holder.bindOptionListener(position, this.itemOptionListener);
        holder.bindItemListener(position,localDataSet.get(position), this.itemClickListener);
    }

    @Override
    public int getItemCount() {
        return localDataSet.size();
    }
}
