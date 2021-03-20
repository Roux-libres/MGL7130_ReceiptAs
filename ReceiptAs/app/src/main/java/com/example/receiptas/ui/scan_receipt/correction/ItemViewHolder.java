package com.example.receiptas.ui.scan_receipt.correction;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.receiptas.R;
import com.example.receiptas.ui.history.OnRecyclerViewItemClickListener;

public class ItemViewHolder extends RecyclerView.ViewHolder {
    private final TextView itemLabel;
    private final Button itemMenu;

    public ItemViewHolder(@NonNull View itemView) {
        super(itemView);
        this.itemLabel = itemView.findViewById(R.id.itemLabel);
        this.itemMenu = itemView.findViewById(R.id.itemMenu);
    }

    public TextView getItemLabel() {
        return this.itemLabel;
    }

    public  Button getItemMenu() {
        return this.itemMenu;
    }

    public void bindListener(final int id, final OnRecyclerViewItemClickListener<View> listener) {
        itemMenu.setOnClickListener(view -> listener.onItemClick(id, view));
    }
}
