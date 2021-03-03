package com.example.receiptas;

import android.content.Context;
import android.widget.ArrayAdapter;
import android.widget.Filter;

import androidx.annotation.NonNull;

import java.util.ArrayList;

public class MaterialDropdownMenuArrayAdapter extends ArrayAdapter {

    private Filter filter;

    public MaterialDropdownMenuArrayAdapter(@NonNull Context context, int resource, @NonNull ArrayList<String> values) {
        super(context, resource, values);

        this.filter = new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults results = new FilterResults();
                results.values = values;
                results.count = values.size();
                return results;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                notifyDataSetChanged();
            }
        };
    }

    @Override
    public Filter getFilter(){
        return this.filter;
    }
}