package com.example.receiptas.ui.history;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.receiptas.R;
import com.example.receiptas.model.domain_model.Receipt;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class ReceiptAdapter extends RecyclerView.Adapter<ReceiptViewHolder> {

    private List<Receipt> localDataSet;
    private final OnRecyclerViewItemClickListener listener;
    private int sortMethod;

    public ReceiptAdapter(List<Receipt> dataSet, OnRecyclerViewItemClickListener listener, int sortMethod) {
        this.localDataSet = dataSet;
        this.listener = listener;
        this.sortMethod = sortMethod;
        this.sortDataSet();
    }

    public void sortDataSet(){
        Comparator<Receipt> comparator;
        boolean reverse = false;

        switch(this.sortMethod){
            case R.id.sort_receipt_alphabetical:
                comparator = (Receipt r1, Receipt r2) -> r1.getName().compareTo(r2.getName());
                break;
            case R.id.sort_receipt_date_recent_first:
                comparator = (Receipt r1, Receipt r2) -> r1.getDate().compareTo(r2.getDate());
                break;
            case R.id.sort_receipt_date_older_first:
                comparator = (Receipt r1, Receipt r2) -> r1.getDate().compareTo(r2.getDate());
                reverse = true;
                break;
            case R.id.sort_receipt_price_ascending:
                comparator = (Receipt r1, Receipt r2) -> Float.compare(r1.getTotalAmount(), r2.getTotalAmount());
                break;
            case R.id.sort_receipt_price_descending:
                comparator = (Receipt r1, Receipt r2) -> Float.compare(r1.getTotalAmount(), r2.getTotalAmount());
                reverse = true;
                break;
            default:
                comparator = null;
                break;
        }

        Collections.sort(this.localDataSet, comparator);

        if(reverse){
            Collections.reverse(this.localDataSet);
        }

        this.notifyDataSetChanged();
    }

    public boolean setSortMethod(int sortMethod){
        if(this.sortMethod != sortMethod){
            this.sortMethod = sortMethod;
            this.sortDataSet();
        }
        return true;
    }

    @NonNull
    @Override
    public ReceiptViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.history_row_item, parent, false);
        return new ReceiptViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReceiptViewHolder holder, int position) {
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        holder.getLeftTextView().setText(localDataSet.get(position).getName());
        holder.getRightTextView().setText(dateFormat.format(localDataSet.get(position).getDate()));
        holder.bindListener(position, localDataSet.get(position).getName(), this.listener);
    }

    @Override
    public int getItemCount() {
        return localDataSet.size();
    }
}
