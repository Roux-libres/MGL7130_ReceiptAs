package com.example.receiptas.ui.scan_receipt.division;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.receiptas.R;
import com.example.receiptas.model.domain_model.Participant;
import com.example.receiptas.model.domain_model.Receipt;

import java.util.ArrayList;
import java.util.Locale;

public class ItemDivisionAdapter extends RecyclerView.Adapter<ItemDivisionViewHolder> {

    private final Receipt receipt;
    private Context context;
    private Participant currentParticipant;
    private int currentColor;

    public ItemDivisionAdapter(Receipt receipt, Context context, Participant currentParticipant) {
        this.receipt = receipt;
        this.context = context;
        this.currentParticipant = currentParticipant;
        if(currentParticipant != null) {
            Resources res = this.context.getResources();
            this.currentColor = res.getColor(res.obtainTypedArray(R.array.colors_participants)
                    .getResourceId(this.receipt.getParticipants().indexOf(currentParticipant), 0));
        }
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
        holder.getItemPrice().setText(
            new StringBuilder()
                .append(this.receipt.getItems().get(position).getPrice())
                .append(this.receipt.getCurrency().getSymbol(Locale.getDefault()))
                .toString()
        );
        holder.itemView.setBackgroundColor(0);

        ArrayList<Participant> itemParticipants = this.receipt.getItems().get(position).getParticipants();
        ArrayList<Participant> participants = this.receipt.getParticipants();

        for (Participant participant : itemParticipants) {
            if (participant == this.currentParticipant) {
                holder.itemView.setBackgroundColor(currentColor);
                holder.itemView.getBackground().setAlpha(50);
            } else {
                holder.getColorParticipantIndicators().get(participants.indexOf(participant)).setVisibility(View.VISIBLE);
            }
        }

        if(this.currentParticipant != null) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(receipt.getItems().get(position).getParticipants().contains(currentParticipant)) {
                        itemParticipants.remove(currentParticipant);
                    } else {
                        itemParticipants.add(currentParticipant);
                    }

                    notifyItemChanged(position);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return this.receipt.getItems().size();
    }
}
