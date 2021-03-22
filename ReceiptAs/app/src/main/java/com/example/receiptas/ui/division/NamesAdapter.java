package com.example.receiptas.ui.division;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.example.receiptas.R;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;

public class NamesAdapter extends BaseAdapter {

    private Context mContext;
    private ArrayList<String> names;
    private ArrayList<Integer> colorsParticipants;


    public NamesAdapter(Context mContext, ArrayList<String> names) {
        this.mContext = mContext;
        this.names = (names == null) ? new ArrayList<String>(): names;
        this.colorsParticipants = new ArrayList<Integer>();

        Resources res = mContext.getResources();
        TypedArray colors = res.obtainTypedArray(R.array.colors_participants);
        for (int i = 0; i < colors.length(); ++i) {
            int id = colors.getResourceId(i, 0);
            if (id > 0) {
                colorsParticipants.add(res.getColor(id));
            }
        }
    }

    public ArrayList<String> getNames() {
        return names;
    }

    public boolean addName(String name) {
        if (this.names.size() < 8) {
            this.names.add(name);
            return true;
        }
        return false;
    }

    @Override
    public int getCount() {
        return names.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            final LayoutInflater layoutInflater = LayoutInflater.from(mContext);
            convertView = layoutInflater.inflate(R.layout.division_name_item, null);
        }


        TextView nameItemText = (TextView) convertView.findViewById(R.id.name_item_text);
        nameItemText.setText(this.names.get(position));

        FrameLayout nameItemColor = (FrameLayout) convertView.findViewById(R.id.name_item_color);
        nameItemColor.setBackgroundTintList(ColorStateList.valueOf(colorsParticipants.get(position)));

        FrameLayout removeNameIcon = (FrameLayout) convertView.findViewById(R.id.remove_name_icon);
        removeNameIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                names.remove(position);
                notifyDataSetChanged();
            }
        });

        return convertView;
    }
}
