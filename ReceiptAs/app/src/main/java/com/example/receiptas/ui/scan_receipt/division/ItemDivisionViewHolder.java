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
    private boolean selected;


    public ItemDivisionViewHolder(@NonNull View itemView) {
        super(itemView);

        this.itemName = itemView.findViewById(R.id.item_name);
        this.itemPrice = itemView.findViewById(R.id.item_price);
        this.selected = false;

        LinearLayout colorsLayout = itemView.findViewById(R.id.colors_views_list);
        this.colorParticipantIndicators = new ArrayList<>();
        for(int i=0; i<colorsLayout.getChildCount(); i++) {
            this.colorParticipantIndicators.add(colorsLayout.getChildAt(i));
        }
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
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

    public void bindListener(final View.OnClickListener listener) {
    //    this.itemView.setOnClickListener(listener);
    }

}
