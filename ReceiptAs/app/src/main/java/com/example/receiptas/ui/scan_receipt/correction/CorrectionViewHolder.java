package com.example.receiptas.ui.scan_receipt.correction;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.receiptas.R;
import com.example.receiptas.ui.history.OnRecyclerViewItemClickListener;

public class CorrectionViewHolder extends RecyclerView.ViewHolder {
    private final TextView leftTextView, rightTextView;

    public CorrectionViewHolder(@NonNull View itemView) {
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

    public void bindListener(final int id, final String item, final OnRecyclerViewItemClickListener listener) {
        itemView.setOnClickListener(view -> listener.onItemClick(id, item));
    }
}
