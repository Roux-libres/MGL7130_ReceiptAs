package com.example.receiptas.ui.division;

import android.content.Context;
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
    private TypedArray colors;

    public ItemDivisionAdapter(Receipt receipt, Context context) {
        this.receipt = receipt;
        this.context = context;
        this.colors = context.getResources().obtainTypedArray(R.array.colors_participants);
    }

    @NonNull
    @Override
    public ItemDivisionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_price_colors, parent, false);
        return new ItemDivisionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemDivisionViewHolder holder, int position) {
        holder.getItemName().setText(this.receipt.getItems().get(position).getName());
        holder.getItemPrice().setText(Float.toString(this.receipt.getItems().get(position).getPrice()));
        for(Participant participant: this.receipt.getItems().get(position).getParticipants())
            holder.getColorParticipantIndicators().get(this.receipt.getParticipants().indexOf(participant)).setVisibility(View.VISIBLE);
       // holder.bindListener(this.listener);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // holder.itemView.setBackgroundColor(context.getResources().getColor(colors.getResourceId(position)));
            }
        });

    }

    @Override
    public int getItemCount() {
        return this.receipt.getItems().size();
    }
}
