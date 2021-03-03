package com.example.receiptas.ui.history;

import android.view.View;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.receiptas.R;

public class ReceiptViewHolder extends RecyclerView.ViewHolder {
    private final TextView leftTextView, rightTextView;

    public ReceiptViewHolder(@NonNull View itemView) {
        super(itemView);
        leftTextView = itemView.findViewById(R.id.leftTextView);
        rightTextView = itemView.findViewById(R.id.rightTextView);
    }

    public TextView getLeftTextView() {
        return leftTextView;
    }

    public TextView getRightTextView() {
        return rightTextView;
    }

    public void bindListener(final String item, final OnRecyclerViewItemClickListener listener) {
        itemView.setOnClickListener(view -> listener.onItemClick(item));
    }
}
