package com.example.receiptas.ui.division;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.receiptas.R;
import com.example.receiptas.model.domain_model.Item;
import com.example.receiptas.model.domain_model.Participant;
import com.example.receiptas.model.domain_model.Receipt;
import com.example.receiptas.ui.history.OnRecyclerViewItemClickListener;
import com.example.receiptas.ui.history.ReceiptViewHolder;

import java.util.ArrayList;
import java.util.List;

public class ItemDivisionAdapter extends RecyclerView.Adapter<ItemDivisionViewHolder> {

    private final Receipt receipt;
    private Context context;
    private Participant currentParticipant;

    public ItemDivisionAdapter(Receipt receipt, Context context, Participant currentParticipant) {
        this.receipt = receipt;
        this.context = context;
        this.currentParticipant = currentParticipant;
    }


    @NonNull
    @Override
    public ItemDivisionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_price_colors, parent, false);
        return new ItemDivisionViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ItemDivisionViewHolder holder, int position) {
        holder.getItemName().setText(this.receipt.getItems().get(position).getName());
        holder.getItemPrice().setText(Float.toString(this.receipt.getItems().get(position).getPrice()));

        ArrayList<Participant> itemParticipants = this.receipt.getItems().get(position).getParticipants();
        ArrayList<Participant> participants = this.receipt.getParticipants();

        if(this.currentParticipant != null) {
            Resources res = this.context.getResources();
            int currentColor = res.getColor(res.obtainTypedArray(R.array.colors_participants).getResourceId(participants.indexOf(currentParticipant), 0));

            for (Participant participant : itemParticipants) {
                if (participant == this.currentParticipant) {
                    holder.itemView.setBackgroundColor(currentColor);
                    holder.itemView.getBackground().setAlpha(50);
                    holder.setSelected(true);
                } else {
                    if (participants.contains(participant))
                        holder.getColorParticipantIndicators().get(participants.indexOf(participant)).setVisibility(View.VISIBLE);
                }
            }

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    holder.setSelected(!holder.isSelected());

                    if(holder.isSelected()) {
                        itemParticipants.add(currentParticipant);
                        holder.itemView.setBackgroundColor(currentColor);
                        holder.itemView.getBackground().setAlpha(50);
                    } else {
                        itemParticipants.remove(currentParticipant);
                        holder.itemView.setBackgroundColor(0);
                    }
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return this.receipt.getItems().size();
    }
}
