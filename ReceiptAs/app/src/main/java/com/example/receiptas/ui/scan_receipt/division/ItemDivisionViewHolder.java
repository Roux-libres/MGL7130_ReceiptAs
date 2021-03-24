package com.example.receiptas.ui.scan_receipt.division;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.receiptas.R;

import java.util.ArrayList;

public class ItemDivisionViewHolder extends RecyclerView.ViewHolder {
    private final TextView itemName, itemPrice;
    private final ArrayList<View> colorParticipantIndicators;

    public ItemDivisionViewHolder(@NonNull View itemView) {
        super(itemView);

        this.itemName = itemView.findViewById(R.id.item_name);
        this.itemPrice = itemView.findViewById(R.id.item_price);

        LinearLayout colorsLayout = itemView.findViewById(R.id.colors_views_list);
        this.colorParticipantIndicators = new ArrayList<>();
        for(int i=0; i<colorsLayout.getChildCount(); i++) {
            this.colorParticipantIndicators.add(colorsLayout.getChildAt(i));
        }
    }

    public TextView getItemName() {
        return itemName;
    }

    public TextView getItemPrice() {
        return itemPrice;
    }

    public ArrayList<View> getColorParticipantIndicators() {
        return colorParticipantIndicators;
    }
}
